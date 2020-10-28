package hr.alenmagdic.carperformancesimulator.domain.usecase.simulation

import hr.alenmagdic.carperformancesimulator.domain.Simulator

class DownshiftUseCase(private val simulator: Simulator) {

    fun execute() {
        simulator.downshift()
    }

}