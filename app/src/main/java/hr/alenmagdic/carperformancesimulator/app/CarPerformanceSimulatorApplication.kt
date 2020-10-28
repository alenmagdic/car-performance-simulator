package hr.alenmagdic.carperformancesimulator.app

import android.app.Application
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component.AppComponent
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component.DaggerAppComponent
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.AppModule

class CarPerformanceSimulatorApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }
}