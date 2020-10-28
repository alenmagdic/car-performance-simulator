package hr.alenmagdic.carperformancesimulator.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PowerAtRpmModel(
    var id: Long = 0,
    var rpm: Double? = null,
    var power: Double? = null
) : Parcelable {

    constructor(powerAtRpmModel: PowerAtRpmModel) : this(
        powerAtRpmModel.id,
        powerAtRpmModel.rpm,
        powerAtRpmModel.power
    )

    fun isModelValid(): Boolean {
        return rpm != null &&
                power != null
    }
}