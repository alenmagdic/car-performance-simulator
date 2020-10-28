package hr.alenmagdic.carperformancesimulator.app.model.mapper

import hr.alenmagdic.carperformancesimulator.app.model.GearRatioModel
import hr.alenmagdic.carperformancesimulator.domain.model.GearRatio
import javax.inject.Inject

class GearRatioMapper @Inject constructor() {

    fun toGearRatio(gearRatioModel: GearRatioModel): GearRatio {
        return GearRatio(
            gearRatioModel.id,
            gearRatioModel.gearNumber!!,
            gearRatioModel.speedKmH!!,
            gearRatioModel.rpmAtSpeed!!
        )
    }

    fun toGearRatioModel(gearRatio: GearRatio): GearRatioModel {
        return GearRatioModel(
            gearRatio.id,
            gearRatio.gearNumber,
            gearRatio.speedKmH,
            gearRatio.rpmAtSpeed
        )
    }

}