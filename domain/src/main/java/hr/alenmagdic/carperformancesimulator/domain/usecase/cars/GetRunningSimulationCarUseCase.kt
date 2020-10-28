package hr.alenmagdic.carperformancesimulator.domain.usecase.cars

import hr.alenmagdic.carperformancesimulator.domain.model.Car
import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository

class GetRunningSimulationCarUseCase(
    private val runningSimulationRepository: RunningSimulationRepository
) {

    fun execute(): Car? {
        return runningSimulationRepository.getRunningSimulation()?.car
    }
}