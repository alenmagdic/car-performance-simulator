package hr.alenmagdic.carperformancesimulator.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransmissionModel(
    var gearChangeLengthMilis: Long? = null,
    var gearRatios: List<GearRatioModel> = emptyList()
) : Parcelable {

    constructor(transmissionModel: TransmissionModel) : this(
        transmissionModel.gearChangeLengthMilis,
        transmissionModel.gearRatios.map { GearRatioModel(it) }
    )

    fun numberOfGears() = gearRatios.size

    fun isModelValid(): Boolean {
        return gearChangeLengthMilis != null &&
                gearRatios.all { it.isModelValid() }
    }
}
