package hr.alenmagdic.carperformancesimulator.domain.usecase.simulation

import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository

class SetThrottleInputUseCase(
    private val runningSimulationRepository: RunningSimulationRepository
) {

    fun execute(throttleInput: Double) {
        runningSimulationRepository.getRunningSimulation()?.setThrottleInput(throttleInput)
    }
}