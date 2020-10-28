package hr.alenmagdic.carperformancesimulator.domain.model

data class AnalogSpeedometer(
    var maxSpeed: Int,
    var segmentsCount: Int,
    var labeledMarksStep: Int
) {

    fun isValid(): Boolean {
        return maxSpeed > 0 &&
                segmentsCount > 0 &&
                labeledMarksStep > 0
    }

}