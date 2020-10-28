package hr.alenmagdic.carperformancesimulator.domain

import hr.alenmagdic.carperformancesimulator.domain.model.Car
import hr.alenmagdic.carperformancesimulator.domain.model.CarInput
import hr.alenmagdic.carperformancesimulator.domain.model.CarState
import hr.alenmagdic.carperformancesimulator.domain.model.Simulation
import java.util.*
import kotlin.math.max

class Simulator {
    @Volatile
    private var stopSimulation = false

    private lateinit var car: Car
    private lateinit var input: CarInput
    private lateinit var state: CarState
    private lateinit var stateBuilder: CarStateBuilder
    private var inputEventsQueue: Queue<InputEventProcessor> = LinkedList()

    fun runSimulation(simulation: Simulation, simulationListener: SimulationListener) {
        this.car = simulation.car
        this.state = simulation.carState
        this.input = simulation.carInput

        var lastUpdateTime = System.currentTimeMillis()
        stopSimulation = false
        while (!stopSimulation) {
            val currentTime = System.currentTimeMillis()
            val timeDiff = currentTime - lastUpdateTime

            updateCarState(timeDiff)
            simulation.carState = state
            simulationListener.onCarStateUpdate(state)

            lastUpdateTime = currentTime

            try {
                Thread.sleep(TIME_BETWEEN_UPDATES_MILIS)
            } catch (ex: InterruptedException) {
            }
        }
    }

    fun stopSimulation() {
        stopSimulation = true
    }

    fun upshift() {
        inputEventsQueue.offer(object :
            InputEventProcessor {
            override fun process(stateBuilder: CarStateBuilder, car: Car) {
                tryToChangeGear(stateBuilder.gear + 1)
            }
        })
    }

    fun downshift() {
        inputEventsQueue.offer(object :
            InputEventProcessor {
                override fun process(stateBuilder: CarStateBuilder, car: Car) {
                    tryToChangeGear(stateBuilder.gear - 1)
                }
        })
    }

    fun createIdleCarState(car: Car): CarState {
        val rpm = car.engine.idleRpm
        val power = car.engine.getPowerAtRpm(rpm)
        val torque = car.engine.getTorqueAtRpm(rpm)

        return CarState(
            rpm = rpm,
            clutchRpm = rpm,
            availablePower = power,
            usedPower = 0.0,
            availableTorque = torque,
            usedTorque = 0.0,
            acceleration = 0.0,
            gear = 0,
            gearChangingProcessRemainingTimeMilis = 0,
            speedKmH = 0.0,
            kineticEnergy = 0.0,
            producedEnergy = 0.0,
            totalDistance = 0.0,
            startingInProgress = false,
            airResistance = 0.0,
            rollResistance = 0.0,
            rollResistancePower = 0.0,
            airResistancePower = 0.0,
            accelerationPower = 0.0
        )
    }

    private fun updateCarState(timeDiffMilis: Long) {
        stateBuilder = CarStateBuilder(
            car,
            input,
            state.rpm,
            state.clutchRpm,
            state.acceleration,
            state.gear,
            state.gearChangingProcessRemainingTimeMilis,
            state.kineticEnergy,
            state.producedEnergy,
            state.totalDistance,
            state.startingInProgress,
            state.rollResistance,
            state.airResistance
        )
        val timeDiffSeconds = timeDiffMilis.toSeconds()

        processPendingInputEvents()
        handleEngineForceEffect(timeDiffSeconds)
        handleResistiveForcesEffect(timeDiffSeconds)
        handleEngineCompressionEffect(timeDiffSeconds)
        handleBrakeInputEffect(timeDiffSeconds)
        handleClutchInputEffect(timeDiffSeconds)
        handleGearChanging(timeDiffMilis)

        val speedDiff = Physics.calculateSpeedMPerSFromKineticEnergy(stateBuilder.kineticEnergy, car.weight) -
                    Physics.calculateSpeedMPerSFromKineticEnergy(state.kineticEnergy, car.weight)
        stateBuilder.acceleration = speedDiff / timeDiffSeconds
        stateBuilder.totalDistance += stateBuilder.speedKmH.toMetersPerSec() * timeDiffSeconds

        state = stateBuilder.build()
    }

    private fun processPendingInputEvents() {
        while (inputEventsQueue.isNotEmpty()) {
            inputEventsQueue.poll()?.process(stateBuilder, car)
        }
    }

    private fun handleEngineForceEffect(timeDiffSeconds: Double) {
        val powerInWatt = car.engine.getPowerAtRpm(stateBuilder.rpm).toWatt()
        val engineKineticEnergyDiff = if (stateBuilder.gear != CarState.NEUTRAL_GEAR) {
            input.throttleInput * powerInWatt * (CarInput.FULL_INPUT - input.clutchInput) * timeDiffSeconds
        } else {
            0.0
        }

        stateBuilder.kineticEnergy += engineKineticEnergyDiff
        stateBuilder.producedEnergy += engineKineticEnergyDiff
    }

    private fun handleResistiveForcesEffect(timeDiffSeconds: Double) {
        if (stateBuilder.gear != CarState.NEUTRAL_GEAR && stateBuilder.rpm <= car.engine.idleRpm) {
            return
        }

        val speedMPerS = Physics.calculateSpeedMPerSFromKineticEnergy(
            stateBuilder.kineticEnergy,
            car.weight
        )
        val speedKmH = speedMPerS.toKmPerHour()

        stateBuilder.rollResistance = Physics.calculateRollResistance(
            car.tirePressure,
            speedKmH,
            car.weight,
            GRAVITY_ACCELERATION
        )
        stateBuilder.airResistance = Physics.calculateAirResistance(
            speedMPerS,
            AIR_DENSITY,
            car.dragCoefficient,
            car.frontArea
        )
        val resistance = stateBuilder.rollResistance + stateBuilder.airResistance
        val deceleration = resistance / car.weight
        val speedDiff = deceleration * timeDiffSeconds

        if (speedMPerS - speedDiff > 0) {
            stateBuilder.kineticEnergy =
                Physics.calculateKineticEnergy(
                    car.weight,
                    speedMPerS - speedDiff
                )
        } else {
            stateBuilder.kineticEnergy = 0.0
        }

    }

    private fun handleEngineCompressionEffect(timeDiffSeconds: Double) {
        if (stateBuilder.rpm <= car.engine.idleRpm) {
            return
        }

        if (input.throttleInput.isEqualTo(CarInput.NO_INPUT) && stateBuilder.gear != CarState.NEUTRAL_GEAR) { //engine compression consumes the kinetic energy because there is no combustion
            stateBuilder.kineticEnergy -= stateBuilder.rpm.toRotationsPerSec() * timeDiffSeconds *
                    car.engine.compressionEnergy * (CarInput.FULL_INPUT - input.clutchInput)
        }

        ensureRpmNotBelowIdleRpm()
    }

    private fun handleBrakeInputEffect(timeDiffSeconds: Double) {
        val speedMPerS = Physics.calculateSpeedMPerSFromKineticEnergy(
            stateBuilder.kineticEnergy,
            car.weight
        )
        val brakeDeceleration = if (speedMPerS > 0) {
            car.brakeForce * input.brakeInput / car.weight
        } else {
            0.0
        }

        val updatedSpeedMPerS = speedMPerS - brakeDeceleration * timeDiffSeconds
        stateBuilder.kineticEnergy = if (updatedSpeedMPerS > 0) {
            Physics.calculateKineticEnergy(
                car.weight,
                updatedSpeedMPerS
            )
        } else {
            0.0
        }
    }

    private fun handleClutchInputEffect(timeDiffSeconds: Double) {
        if (stateBuilder.gear != CarState.NEUTRAL_GEAR && input.clutchInput < CarInput.FULL_INPUT) {
            if (stateBuilder.startingInProgress) {
                stateBuilder.clutchRpm = getRpmWithClutchPressed(timeDiffSeconds, CarInput.FULL_INPUT)
            }
            stateBuilder.rpm = getRpmWithClutchPressed(timeDiffSeconds, input.clutchInput)

            if (stateBuilder.rpm < car.engine.idleRpm * MIN_POSSIBLE_RPM_FACTOR ||
                stateBuilder.rpm < (car.engine.idleRpm - MIN_POSSIBLE_RELATIVE_RPM_WITHOUT_THROTTLE) &&
                input.throttleInput.isEqualTo(CarInput.NO_INPUT)
            ) {
                changeGear(CarState.NEUTRAL_GEAR)
                stateBuilder.rpm = car.engine.idleRpm
                stateBuilder.clutchRpm = stateBuilder.rpm
            }
        } else if (stateBuilder.gear == CarState.NEUTRAL_GEAR ||
            input.clutchInput.isEqualTo(CarInput.FULL_INPUT)
        ) {
            stateBuilder.rpm = getRpmWithClutchPressed(timeDiffSeconds, CarInput.FULL_INPUT)
            stateBuilder.clutchRpm = stateBuilder.rpm
        }
    }

    private fun handleGearChanging(timeDiffMilis: Long) {
        if (stateBuilder.startingInProgress) {
            updateStartingProcess(timeDiffMilis)
        } else if (input.clutchInput.isEqualTo(CarInput.FULL_INPUT)) {

            stateBuilder.gearChangingProcessRemainingTimeMilis =
                max(0, stateBuilder.gearChangingProcessRemainingTimeMilis - timeDiffMilis)

            if (stateBuilder.gearChangingProcessRemainingTimeMilis == 0L) {
                input.clutchInput = CarInput.NO_INPUT
            }
        }
    }

    private fun tryToChangeGear(newGear: Int) {
        if (newGear < 0) {
            return
        }

        if (newGear == CarState.NEUTRAL_GEAR) {
            changeGear(CarState.NEUTRAL_GEAR)
            stateBuilder.rpm = car.engine.idleRpm
        } else if (newGear == CarState.FIRST_GEAR) {
            val rpmInNewGear = car.getRpmAtSpeed(stateBuilder.speedKmH, newGear)
            if (rpmInNewGear <= car.engine.maxRpm) {
                if (stateBuilder.gear != CarState.NEUTRAL_GEAR || stateBuilder.gear == CarState.NEUTRAL_GEAR && input.throttleInput > CarInput.NO_INPUT) {
                    if (stateBuilder.gear == CarState.NEUTRAL_GEAR) {
                        startDrive()
                    }
                    changeGear(newGear)
                }
            }
        } else if (car.transmission.gearRatios.size >= newGear) {
            val rpmInNewGear = car.getRpmAtSpeed(stateBuilder.speedKmH, newGear)
            if (rpmInNewGear <= car.engine.maxRpm && rpmInNewGear >= car.engine.idleRpm) {
                changeGear(newGear)
            }
        }

    }

    private fun changeGear(newGear: Int) {
        if (stateBuilder.gear != CarState.NEUTRAL_GEAR) {
            stateBuilder.clutchRpm = stateBuilder.rpm
        }

        input.clutchInput = CarInput.FULL_INPUT
        stateBuilder.gear = newGear
        if (!stateBuilder.startingInProgress) {
            stateBuilder.gearChangingProcessRemainingTimeMilis = car.transmission.gearChangeLengthMilis
        }
    }

    private fun startDrive() {
        stateBuilder.startingInProgress = true
        input.clutchInput = CarInput.FULL_INPUT
    }

    private fun updateStartingProcess(timeDiffMilis: Long) {
        val inputDiff = stateBuilder.rpm / car.engine.maxRpm * CLUTCH_RELEASE_RATE * timeDiffMilis
        input.clutchInput -= inputDiff

        if (input.clutchInput <= CarInput.NO_INPUT) {
            input.clutchInput = CarInput.NO_INPUT
            stateBuilder.startingInProgress = false
            stateBuilder.clutchRpm = car.engine.idleRpm
        }
    }

    private fun getRpmWithClutchPressed(timeDiffSeconds: Double, clutchInput: Double): Double {
        val rpmWithoutClutch = car.getRpmAtSpeed(stateBuilder.speedKmH, stateBuilder.gear)
        if (clutchInput.isEqualTo(CarInput.NO_INPUT)) {
            return rpmWithoutClutch
        }

        var rpm = stateBuilder.clutchRpm
        val idleRpm = car.engine.idleRpm
        val maxRpm = car.engine.maxRpm
        if (stateBuilder.gear == CarState.NEUTRAL_GEAR || clutchInput.isEqualTo(CarInput.FULL_INPUT)) {
            if ((rpm - idleRpm) / (maxRpm - idleRpm) <= input.throttleInput) { //there is an assumption that for the rpm in neutral can be used this equation: throttleInput*rpmMax, i.e. if throttle is pressed to the 60% of the maximum input, then the rpm should get to 60% of the maximum rpm
                rpm += (input.throttleInput) * CLUTCH_RPM_GROWTH_RATE * timeDiffSeconds
                if ((rpm - idleRpm) / (maxRpm - idleRpm) > input.throttleInput) {
                    rpm = input.throttleInput * (maxRpm - idleRpm) + idleRpm
                }
            } else {
                rpm -= CLUTCH_RPM_FALL_RATE * timeDiffSeconds
                if (rpm < idleRpm) {
                    rpm = idleRpm
                }
            }
            return rpm
        }

        return stateBuilder.clutchRpm * clutchInput + rpmWithoutClutch * (CarInput.FULL_INPUT - clutchInput)
    }

    private fun ensureRpmNotBelowIdleRpm() { // making sure that rpm doesn't fall below idle rpm - in the real car, this is done by injecting some fuel in order to generate enough power to maintain idle rpm, but in this simulation it is done just by setting kinetic energy to the appropriate value
        val speedAtMinRpm = car.getSpeedKmhAtRpm(car.engine.idleRpm, stateBuilder.gear).toMetersPerSec()
        val kineticEnergyAtMinRpm = Physics.calculateKineticEnergy(car.weight, speedAtMinRpm)

        if (stateBuilder.gear != CarState.NEUTRAL_GEAR && stateBuilder.kineticEnergy < kineticEnergyAtMinRpm) {
            stateBuilder.kineticEnergy = kineticEnergyAtMinRpm
        }
    }

    companion object {
        const val GRAVITY_ACCELERATION = 9.81
        const val AIR_DENSITY = 1.2041
        const val CLUTCH_RPM_GROWTH_RATE = 10000
        const val CLUTCH_RPM_FALL_RATE = 1250
        const val MIN_POSSIBLE_RPM_FACTOR = 0.65 // relative to idle rpm
        const val MIN_POSSIBLE_RELATIVE_RPM_WITHOUT_THROTTLE = 10 // relative to idle rpm
        const val CLUTCH_RELEASE_RATE = 0.0015
        const val TIME_BETWEEN_UPDATES_MILIS = 3L
    }

    interface SimulationListener {
        fun onCarStateUpdate(carState: CarState)
    }

    interface InputEventProcessor {
        fun process(stateBuilder: CarStateBuilder, car: Car)
    }

    class CarStateBuilder(
        var car: Car,
        var carInput: CarInput,
        var rpm: Double,
        var clutchRpm: Double,
        var acceleration: Double,
        var gear: Int,
        var gearChangingProcessRemainingTimeMilis: Long,
        var kineticEnergy: Double,
        var producedEnergy: Double,
        var totalDistance: Double,
        var startingInProgress: Boolean,
        var rollResistance: Double,
        var airResistance: Double
    ) {

        val speedKmH: Double
            get() = Physics.calculateSpeedMPerSFromKineticEnergy(
                kineticEnergy,
                car.weight
            ).toKmPerHour()

        private val availablePower: Double
            get() = car.engine.getPowerAtRpm(rpm)

        private val usedPower: Double
            get() = availablePower * carInput.throttleInput

        private val availableTorque: Double
            get() = car.engine.getTorqueAtRpm(rpm)

        private val usedTorque: Double
            get() = availableTorque * carInput.throttleInput

        private val rollResistancePower: Double
            get() = (rollResistance * speedKmH.toMetersPerSec()).toKilowatt()

        private val airResistancePower: Double
            get() = (airResistance * speedKmH.toMetersPerSec()).toKilowatt()

        private val accelerationPower: Double
            get() = usedPower - rollResistancePower - airResistancePower

        fun build(): CarState {
            return CarState(
                rpm,
                clutchRpm,
                availablePower,
                usedPower,
                availableTorque,
                usedTorque,
                acceleration,
                gear,
                gearChangingProcessRemainingTimeMilis,
                speedKmH,
                kineticEnergy,
                producedEnergy,
                totalDistance,
                startingInProgress,
                rollResistance,
                airResistance,
                rollResistancePower,
                airResistancePower,
                accelerationPower
            )
        }
    }

}