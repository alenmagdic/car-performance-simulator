package hr.alenmagdic.carperformancesimulator.domain.model

data class PowerAtRpm(
    var id: Long,
    var rpm: Double,
    var power: Double
) {
    fun isValid(): Boolean {
        return rpm >= 0 &&
                power >= 0
    }
}