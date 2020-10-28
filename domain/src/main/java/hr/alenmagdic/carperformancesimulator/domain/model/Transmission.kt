package hr.alenmagdic.carperformancesimulator.domain.model

data class Transmission(
    var gearChangeLengthMilis: Long,
    var gearRatios: List<GearRatio>
) {
    fun isValid(): Boolean {
        return gearChangeLengthMilis >= 0 &&
                gearRatios.isNotEmpty() &&
                gearRatios.all { it.isValid() }
    }
}