package hr.alenmagdic.carperformancesimulator.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GearRatioModel(
    var id: Long = 0,
    var gearNumber: Int? = null,
    var speedKmH: Double? = null,
    var rpmAtSpeed: Double? = null
) : Parcelable {

    constructor(gearRatioModel: GearRatioModel) : this(
        gearRatioModel.id,
        gearRatioModel.gearNumber,
        gearRatioModel.speedKmH,
        gearRatioModel.rpmAtSpeed
    )

    fun isModelValid(): Boolean {
        return speedKmH != null &&
                rpmAtSpeed != null
    }
}