package hr.alenmagdic.carperformancesimulator.app.model.mapper

import hr.alenmagdic.carperformancesimulator.app.model.EngineModel
import hr.alenmagdic.carperformancesimulator.domain.model.Engine
import javax.inject.Inject

class EngineMapper @Inject constructor(private val powerAtRpmMapper: PowerAtRpmMapper) {

    fun toEngine(engineModel: EngineModel): Engine {
        return Engine(
            engineModel.maxRpm!!,
            engineModel.idleRpm!!,
            engineModel.compressionEnergy!!,
            engineModel.power.map { powerAtRpmMapper.toPowerAtRpm(it) }
        )
    }

    fun toEngineModel(engine: Engine): EngineModel {
        return EngineModel(
            engine.maxRpm,
            engine.idleRpm,
            engine.compressionEnergy,
            engine.power.map { powerAtRpmMapper.toPowerAtRpmModel(it) }
        )
    }
}