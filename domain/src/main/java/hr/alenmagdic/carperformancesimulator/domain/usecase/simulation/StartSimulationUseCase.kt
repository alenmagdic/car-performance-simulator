package hr.alenmagdic.carperformancesimulator.domain.usecase.simulation

import hr.alenmagdic.carperformancesimulator.domain.Simulator
import hr.alenmagdic.carperformancesimulator.domain.model.CarInput
import hr.alenmagdic.carperformancesimulator.domain.model.CarState
import hr.alenmagdic.carperformancesimulator.domain.model.Simulation
import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.GetCarsUseCase
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.Exception

class StartSimulationUseCase(
    private val runningSimulationRepository: RunningSimulationRepository,
    private val getCarsUseCase: GetCarsUseCase,
    private val simulator: Simulator
) {

    fun execute(): Single<Observable<CarState>> {
        return Single.fromCallable {
            var runningSimulation = runningSimulationRepository.getRunningSimulation()
            if (runningSimulation == null) {
                val allCars = getCarsUseCase.execute().blockingGet()
                if (allCars.isEmpty()) {
                    throw NoAvailableCarException()
                }

                val simulationCar = allCars.first()
                val idleCarState = simulator.createIdleCarState(simulationCar)

                runningSimulation = Simulation(simulationCar, CarInput(), idleCarState)
                runningSimulationRepository.setRunningSimulation(runningSimulation)
            }

            Observable.create { emitter ->
                simulator.runSimulation(runningSimulation,
                    object : Simulator.SimulationListener {
                        override fun onCarStateUpdate(carState: CarState) {
                            emitter.onNext(carState)
                        }
                    })
            }
        }

    }

    class NoAvailableCarException : Exception()
}