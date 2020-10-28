package hr.alenmagdic.carperformancesimulator.domain.usecase.simulation

import hr.alenmagdic.carperformancesimulator.domain.Simulator

class UpshiftUseCase(private val simulator: Simulator) {

    fun execute() {
        simulator.upshift()
    }

}