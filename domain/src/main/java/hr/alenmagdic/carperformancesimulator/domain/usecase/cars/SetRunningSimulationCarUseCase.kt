package hr.alenmagdic.carperformancesimulator.domain.usecase.cars

import hr.alenmagdic.carperformancesimulator.domain.Simulator
import hr.alenmagdic.carperformancesimulator.domain.model.Car
import hr.alenmagdic.carperformancesimulator.domain.model.CarInput
import hr.alenmagdic.carperformancesimulator.domain.model.Simulation
import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository

class SetRunningSimulationCarUseCase(
    private val runningSimulationRepository: RunningSimulationRepository,
    private val simulator: Simulator
) {

    fun execute(car: Car) {
        val updatedSimulation = Simulation(car, CarInput(), simulator.createIdleCarState(car))
        runningSimulationRepository.setRunningSimulation(updatedSimulation)
    }
}