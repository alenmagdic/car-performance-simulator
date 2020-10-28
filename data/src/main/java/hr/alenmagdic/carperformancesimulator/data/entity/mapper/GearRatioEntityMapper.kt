package hr.alenmagdic.carperformancesimulator.data.entity.mapper

import hr.alenmagdic.carperformancesimulator.data.entity.GearRatioEntity
import hr.alenmagdic.carperformancesimulator.domain.model.GearRatio

class GearRatioEntityMapper {

    fun toGearRatioEntity(gearRatio: GearRatio, carId: Long): GearRatioEntity {
        return GearRatioEntity(
            gearRatio.id,
            gearRatio.gearNumber,
            carId,
            gearRatio.speedKmH,
            gearRatio.rpmAtSpeed
        )
    }

    fun toGearRatio(gearRatioEntity: GearRatioEntity): GearRatio {
        return GearRatio(
            gearRatioEntity.id,
            gearRatioEntity.gearNumber,
            gearRatioEntity.speedKmh,
            gearRatioEntity.rpmAtSpeed
        )
    }

}