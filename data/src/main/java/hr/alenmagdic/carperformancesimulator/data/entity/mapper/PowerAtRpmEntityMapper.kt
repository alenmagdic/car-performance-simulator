package hr.alenmagdic.carperformancesimulator.data.entity.mapper

import hr.alenmagdic.carperformancesimulator.data.entity.PowerAtRpmEntity
import hr.alenmagdic.carperformancesimulator.domain.model.PowerAtRpm

class PowerAtRpmEntityMapper {

    fun toPowerAtRpmEntity(powerAtRpm: PowerAtRpm, carId: Long): PowerAtRpmEntity {
        return PowerAtRpmEntity(
            powerAtRpm.id,
            carId,
            powerAtRpm.power,
            powerAtRpm.rpm
        )
    }

    fun toPowerAtRpm(powerAtRpmEntity: PowerAtRpmEntity): PowerAtRpm {
        return PowerAtRpm(
            powerAtRpmEntity.id,
            powerAtRpmEntity.rpm,
            powerAtRpmEntity.power
        )
    }
}