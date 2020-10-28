package hr.alenmagdic.carperformancesimulator.app.model.mapper

import hr.alenmagdic.carperformancesimulator.app.model.AnalogRpmMeterModel
import hr.alenmagdic.carperformancesimulator.domain.model.AnalogRpmMeter
import javax.inject.Inject

class AnalogRpmMeterMapper @Inject constructor() {

    fun toAnalogRpmMeter(analogRpmMeterModel: AnalogRpmMeterModel): AnalogRpmMeter {
        return AnalogRpmMeter(
            analogRpmMeterModel.maxRpm!!,
            analogRpmMeterModel.segmentsCount!!,
            analogRpmMeterModel.labeledMarksStep!!,
            analogRpmMeterModel.criticalRpmThreshold!!,
            analogRpmMeterModel.valueLabelMultiplicator!!
        )
    }

    fun toAnalogRpmMeterModel(analogRpmMeter: AnalogRpmMeter): AnalogRpmMeterModel {
        return AnalogRpmMeterModel(
            analogRpmMeter.maxRpm,
            analogRpmMeter.segmentsCount,
            analogRpmMeter.labeledMarksStep,
            analogRpmMeter.criticalRpmThreshold,
            analogRpmMeter.valueLabelMultiplicator
        )
    }

}