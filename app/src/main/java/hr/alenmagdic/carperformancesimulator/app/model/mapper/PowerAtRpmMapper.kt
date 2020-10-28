package hr.alenmagdic.carperformancesimulator.app.model.mapper

import hr.alenmagdic.carperformancesimulator.app.model.PowerAtRpmModel
import hr.alenmagdic.carperformancesimulator.domain.model.PowerAtRpm
import javax.inject.Inject

class PowerAtRpmMapper @Inject constructor() {

    fun toPowerAtRpm(powerAtRpmModel: PowerAtRpmModel): PowerAtRpm {
        return PowerAtRpm(
            powerAtRpmModel.id,
            powerAtRpmModel.rpm!!,
            powerAtRpmModel.power!!
        )
    }

    fun toPowerAtRpmModel(powerAtRpm: PowerAtRpm): PowerAtRpmModel {
        return PowerAtRpmModel(
            powerAtRpm.id,
            powerAtRpm.rpm,
            powerAtRpm.power
        )
    }
}