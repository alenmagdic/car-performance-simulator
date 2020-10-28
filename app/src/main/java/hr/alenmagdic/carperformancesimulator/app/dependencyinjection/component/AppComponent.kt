package hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component

import dagger.Component
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.AppModule
import hr.alenmagdic.carperformancesimulator.domain.Simulator
import hr.alenmagdic.carperformancesimulator.domain.repository.CarRepository
import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun runningSimulationRepository(): RunningSimulationRepository
    fun carRepository(): CarRepository
    fun simulator(): Simulator
}