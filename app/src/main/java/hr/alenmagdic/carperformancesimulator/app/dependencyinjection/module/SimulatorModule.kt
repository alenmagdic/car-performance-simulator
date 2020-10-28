package hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module

import dagger.Module
import dagger.Provides
import hr.alenmagdic.carperformancesimulator.app.contracts.SimulatorContract
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.ActivityScope
import hr.alenmagdic.carperformancesimulator.app.model.mapper.CarMapper
import hr.alenmagdic.carperformancesimulator.app.presenter.SimulatorPresenter
import hr.alenmagdic.carperformancesimulator.domain.Simulator
import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.GetCarsUseCase
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.GetRunningSimulationCarUseCase
import hr.alenmagdic.carperformancesimulator.domain.usecase.simulation.*

@Module
class SimulatorModule {

    @Provides
    @ActivityScope
    fun provideSimulatorPresenter(
        setThrottleInputUseCase: SetThrottleInputUseCase,
        setBrakeInputUseCase: SetBrakeInputUseCase,
        upshiftUseCase: UpshiftUseCase,
        downshiftUseCase: DownshiftUseCase,
        startSimulationUseCase: StartSimulationUseCase,
        stopSimulationUseCase: StopSimulationUseCase,
        getRunningSimulationCarUseCase: GetRunningSimulationCarUseCase,
        carMapper: CarMapper
    ): SimulatorContract.PresenterInterface {

        return SimulatorPresenter(
            setThrottleInputUseCase,
            setBrakeInputUseCase,
            upshiftUseCase,
            downshiftUseCase,
            startSimulationUseCase,
            stopSimulationUseCase,
            getRunningSimulationCarUseCase,
            carMapper
        )
    }

    @Provides
    @ActivityScope
    fun provideSetThrottleInputUseCase(
        runningSimulationRepository: RunningSimulationRepository
    ): SetThrottleInputUseCase {
        return SetThrottleInputUseCase(runningSimulationRepository)
    }

    @Provides
    @ActivityScope
    fun provideSetBrakeInputUseCase(
        runningSimulationRepository: RunningSimulationRepository
    ): SetBrakeInputUseCase {
        return SetBrakeInputUseCase(runningSimulationRepository)
    }

    @Provides
    @ActivityScope
    fun provideUpshiftUseCase(
        simulator: Simulator
    ): UpshiftUseCase {
        return UpshiftUseCase(simulator)
    }

    @Provides
    @ActivityScope
    fun provideDownshiftUseCase(
        simulator: Simulator
    ): DownshiftUseCase {
        return DownshiftUseCase(simulator)
    }

    @Provides
    @ActivityScope
    fun provideStartSimulationUseCase(
        runningSimulationRepository: RunningSimulationRepository,
        getCarsUseCase: GetCarsUseCase,
        simulator: Simulator
    ): StartSimulationUseCase {

        return StartSimulationUseCase(
            runningSimulationRepository,
            getCarsUseCase,
            simulator
        )
    }

    @Provides
    @ActivityScope
    fun provideStopSimulationUseCase(simulator: Simulator): StopSimulationUseCase {
        return StopSimulationUseCase(simulator)
    }

}