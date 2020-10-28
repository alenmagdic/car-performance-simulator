package hr.alenmagdic.carperformancesimulator.domain.repository

import hr.alenmagdic.carperformancesimulator.domain.model.Simulation

interface RunningSimulationRepository {
    fun setRunningSimulation(simulation: Simulation)
    fun getRunningSimulation(): Simulation?
}