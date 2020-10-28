package hr.alenmagdic.carperformancesimulator.domain.model

class CarState(
    val rpm: Double,
    val clutchRpm: Double,
    val availablePower: Double,
    val usedPower: Double,
    val availableTorque: Double,
    val usedTorque: Double,
    val acceleration: Double,
    val gear: Int,
    val gearChangingProcessRemainingTimeMilis: Long,
    val speedKmH: Double,
    val kineticEnergy: Double,
    val producedEnergy: Double,
    val totalDistance: Double,
    val startingInProgress: Boolean,
    val rollResistance: Double,
    val airResistance: Double,
    val rollResistancePower: Double,
    val airResistancePower: Double,
    val accelerationPower: Double
) {
    companion object {
        const val NEUTRAL_GEAR = 0
        const val FIRST_GEAR = 1
    }
}