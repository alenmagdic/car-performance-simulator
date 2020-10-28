package hr.alenmagdic.carperformancesimulator.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnalogRpmMeterModel(
    var maxRpm: Int? = null,
    var segmentsCount: Int? = null,
    var labeledMarksStep: Int? = null,
    var criticalRpmThreshold: Int? = null,
    var valueLabelMultiplicator: Int? = null
) : Parcelable {

    constructor(analogRpmMeterModel: AnalogRpmMeterModel) : this(
        analogRpmMeterModel.maxRpm,
        analogRpmMeterModel.segmentsCount,
        analogRpmMeterModel.labeledMarksStep,
        analogRpmMeterModel.criticalRpmThreshold,
        analogRpmMeterModel.valueLabelMultiplicator
    )

    fun isValid(): Boolean {
        return maxRpm != null &&
                segmentsCount != null &&
                labeledMarksStep != null &&
                criticalRpmThreshold != null &&
                valueLabelMultiplicator != null
    }

}