package hr.alenmagdic.carperformancesimulator.domain.model

data class Simulation(
    val car: Car,
    val carInput: CarInput,
    var carState: CarState
) {
    fun setThrottleInput(throttleInput: Double) {
        carInput.throttleInput = throttleInput
    }

    fun setBrakeInput(brakeInput: Double) {
        carInput.brakeInput = brakeInput
    }
}