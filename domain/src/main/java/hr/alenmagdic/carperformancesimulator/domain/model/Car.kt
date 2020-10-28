package hr.alenmagdic.carperformancesimulator.domain.model

data class Car(
    var id: Long,
    var name: String,
    var transmission: Transmission,
    var engine: Engine,
    var weight: Double,
    var dragCoefficient: Double,
    var frontArea: Double,
    var tirePressure: Double,
    var brakeForce: Int,
    var analogSpeedometer: AnalogSpeedometer,
    var analogRpmMeter: AnalogRpmMeter
) {

    fun getSpeedKmhAtRpm(rpm: Double, gear: Int): Double {
        if (gear == 0) return 0.0
        return transmission.gearRatios[gear - 1].ratio * rpm
    }

    fun getRpmAtSpeed(speedKmH: Double, gear: Int): Double {
        if (gear == 0) return engine.idleRpm
        return speedKmH / transmission.gearRatios[gear - 1].ratio
    }

    fun isValid(): Boolean {
        return name.isNotBlank() &&
                transmission.isValid() &&
                engine.isValid() &&
                weight > 0 &&
                dragCoefficient >= 0 &&
                frontArea >= 0 &&
                tirePressure > 0 &&
                brakeForce >= 0 &&
                analogSpeedometer.isValid() &&
                analogRpmMeter.isValid()
    }

}