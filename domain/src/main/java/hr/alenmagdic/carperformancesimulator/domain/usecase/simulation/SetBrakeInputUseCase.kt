package hr.alenmagdic.carperformancesimulator.domain.usecase.simulation

import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository

class SetBrakeInputUseCase(
    private val runningSimulationRepository: RunningSimulationRepository
) {

    fun execute(brakeInput: Double) {
        runningSimulationRepository.getRunningSimulation()?.setBrakeInput(brakeInput)
    }
}