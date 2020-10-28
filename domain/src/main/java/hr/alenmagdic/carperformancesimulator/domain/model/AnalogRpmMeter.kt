package hr.alenmagdic.carperformancesimulator.domain.model

data class AnalogRpmMeter(
    var maxRpm: Int,
    var segmentsCount: Int,
    var labeledMarksStep: Int,
    var criticalRpmThreshold: Int,
    var valueLabelMultiplicator: Int
) {

    fun isValid(): Boolean {
        return maxRpm > 0 &&
                segmentsCount > 0 &&
                labeledMarksStep > 0 &&
                criticalRpmThreshold >= 0 &&
                valueLabelMultiplicator > 0
    }

}