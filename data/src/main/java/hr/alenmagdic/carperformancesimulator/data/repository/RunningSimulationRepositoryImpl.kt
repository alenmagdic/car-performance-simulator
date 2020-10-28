package hr.alenmagdic.carperformancesimulator.data.repository

import hr.alenmagdic.carperformancesimulator.domain.model.Simulation
import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository

class RunningSimulationRepositoryImpl : RunningSimulationRepository {
    private var simulation: Simulation? = null

    override fun getRunningSimulation() = simulation

    override fun setRunningSimulation(simulation: Simulation) {
        this.simulation = simulation
    }
}