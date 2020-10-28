package hr.alenmagdic.carperformancesimulator.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EngineModel(
    var maxRpm: Double? = null,
    var idleRpm: Double? = null,
    var compressionEnergy: Double? = null,
    var power: List<PowerAtRpmModel> = emptyList()
) : Parcelable {

    constructor(engineModel: EngineModel) : this(
        engineModel.maxRpm,
        engineModel.idleRpm,
        engineModel.compressionEnergy,
        engineModel.power.map { PowerAtRpmModel(it) }
    )

    fun getMaxPower() = power.maxByOrNull { it.power ?: 0.0 }

    fun isModelValid(): Boolean {
        return maxRpm != null &&
                idleRpm != null &&
                compressionEnergy != null &&
                power.all { it.isModelValid() }
    }
}
