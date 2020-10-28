package hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component

import dagger.Component
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.ActivityScope
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.CarModule
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.SimulatorModule
import hr.alenmagdic.carperformancesimulator.app.ui.activity.MainActivity

@ActivityScope
@Component(
    modules = [SimulatorModule::class, CarModule::class],
    dependencies = [AppComponent::class]
)
interface SimulatorComponent {
    fun inject(mainActivity: MainActivity)
}