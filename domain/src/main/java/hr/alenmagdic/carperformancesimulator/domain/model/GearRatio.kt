package hr.alenmagdic.carperformancesimulator.domain.model

data class GearRatio(
    var id: Long,
    var gearNumber: Int,
    val speedKmH: Double,
    val rpmAtSpeed: Double
) {
    val ratio = speedKmH / rpmAtSpeed

    fun isValid(): Boolean {
        return speedKmH > 0 &&
                rpmAtSpeed > 0
    }
}