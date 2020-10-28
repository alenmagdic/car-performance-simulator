package hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component

import dagger.Component
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.ActivityScope
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.FragmentScope
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.CarModule
import hr.alenmagdic.carperformancesimulator.app.ui.activity.CarsListActivity
import hr.alenmagdic.carperformancesimulator.app.ui.activity.EditCarActivity
import hr.alenmagdic.carperformancesimulator.app.ui.activity.SimulationCarSettingsActivity
import hr.alenmagdic.carperformancesimulator.app.ui.fragment.CarDetailsFragment
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.GetCarsUseCase

@ActivityScope
@FragmentScope
@Component(modules = [CarModule::class], dependencies = [AppComponent::class])
interface CarComponent {
    fun inject(simulationCarSettingsActivity: SimulationCarSettingsActivity)
    fun inject(carsListActivity: CarsListActivity)
    fun inject(editCarActivity: EditCarActivity)
    fun inject(carDetailsFragment: CarDetailsFragment)

    fun getCarsUseCase(): GetCarsUseCase
}