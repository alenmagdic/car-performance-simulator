package hr.alenmagdic.carperformancesimulator.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnalogSpeedometerModel(
    var maxSpeed: Int? = null,
    var segmentsCount: Int? = null,
    var labeledMarksStep: Int? = null
) : Parcelable {

    constructor(analogSpeedometerModel: AnalogSpeedometerModel) : this(
        analogSpeedometerModel.maxSpeed,
        analogSpeedometerModel.segmentsCount,
        analogSpeedometerModel.labeledMarksStep
    )

    fun isValid(): Boolean {
        return maxSpeed != null &&
                segmentsCount != null &&
                labeledMarksStep != null
    }

}