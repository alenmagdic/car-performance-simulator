package hr.alenmagdic.carperformancesimulator.app.model.mapper

import hr.alenmagdic.carperformancesimulator.app.model.AnalogSpeedometerModel
import hr.alenmagdic.carperformancesimulator.domain.model.AnalogSpeedometer
import javax.inject.Inject

class AnalogSpeedometerMapper @Inject constructor() {

    fun toAnalogSpeedometer(analogSpeedometerModel: AnalogSpeedometerModel): AnalogSpeedometer {
        return AnalogSpeedometer(
            analogSpeedometerModel.maxSpeed!!,
            analogSpeedometerModel.segmentsCount!!,
            analogSpeedometerModel.labeledMarksStep!!
        )
    }

    fun toAnalogSpeedometerModel(analogSpeedometer: AnalogSpeedometer): AnalogSpeedometerModel {
        return AnalogSpeedometerModel(
            analogSpeedometer.maxSpeed,
            analogSpeedometer.segmentsCount,
            analogSpeedometer.labeledMarksStep
        )
    }

}