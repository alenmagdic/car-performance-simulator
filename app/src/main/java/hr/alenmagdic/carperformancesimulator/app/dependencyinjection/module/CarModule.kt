package hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module

import dagger.Module
import dagger.Provides
import hr.alenmagdic.carperformancesimulator.app.contracts.CarDetailsContract
import hr.alenmagdic.carperformancesimulator.app.contracts.CarsListContract
import hr.alenmagdic.carperformancesimulator.app.contracts.EditCarContract
import hr.alenmagdic.carperformancesimulator.app.contracts.SimulationCarSettingsContract
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.ActivityScope
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.FragmentScope
import hr.alenmagdic.carperformancesimulator.app.model.mapper.CarMapper
import hr.alenmagdic.carperformancesimulator.app.presenter.CarDetailsPresenter
import hr.alenmagdic.carperformancesimulator.app.presenter.CarsListPresenter
import hr.alenmagdic.carperformancesimulator.app.presenter.EditCarPresenter
import hr.alenmagdic.carperformancesimulator.app.presenter.SimulationCarSettingsPresenter
import hr.alenmagdic.carperformancesimulator.domain.Simulator
import hr.alenmagdic.carperformancesimulator.domain.repository.CarRepository
import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.*

@Module
class CarModule {

    @Provides
    @ActivityScope
    fun provideSimulationCarSettingsPresenter(
        getRunningSimulationCarUseCase: GetRunningSimulationCarUseCase,
        setRunningSimulationCarUseCase: SetRunningSimulationCarUseCase,
        carMapper: CarMapper,
        getCarsUseCase: GetCarsUseCase
    ): SimulationCarSettingsContract.PresenterInterface {

        return SimulationCarSettingsPresenter(
            getRunningSimulationCarUseCase,
            setRunningSimulationCarUseCase,
            carMapper,
            getCarsUseCase
        )
    }

    @Provides
    @ActivityScope
    fun provideCarsListPresenter(
        carMapper: CarMapper,
        getCarsUseCase: GetCarsUseCase,
        removeCarUseCase: RemoveCarUseCase
    ): CarsListContract.PresenterInterface {
        return CarsListPresenter(getCarsUseCase, removeCarUseCase, carMapper)
    }

    @Provides
    @ActivityScope
    fun provideEditCarPresenter(
        carMapper: CarMapper,
        getCarDetailsUseCase: GetCarDetailsUseCase,
        saveCarUseCase: SaveCarUseCase
    ): EditCarContract.PresenterInterface {
        return EditCarPresenter(getCarDetailsUseCase, saveCarUseCase, carMapper)
    }

    @Provides
    @ActivityScope
    fun provideGetCarsUseCase(carRepository: CarRepository): GetCarsUseCase {
        return GetCarsUseCase(carRepository)
    }

    @Provides
    @ActivityScope
    fun provideGetCarDetailsUseCase(carRepository: CarRepository): GetCarDetailsUseCase {
        return GetCarDetailsUseCase(carRepository)
    }

    @Provides
    @ActivityScope
    fun provideSaveCarUseCase(carRepository: CarRepository): SaveCarUseCase {
        return SaveCarUseCase(carRepository)
    }

    @Provides
    @ActivityScope
    fun provideRemoveCarUseCase(carRepository: CarRepository): RemoveCarUseCase {
        return RemoveCarUseCase(carRepository)
    }

    @Provides
    @ActivityScope
    fun provideGetRunningSimulationCarUseCase(
        runningSimulationRepository: RunningSimulationRepository
    ): GetRunningSimulationCarUseCase {
        return GetRunningSimulationCarUseCase(runningSimulationRepository)
    }

    @Provides
    @ActivityScope
    fun provideSetRunningSimulationCarUseCase(
        runningSimulationRepository: RunningSimulationRepository,
        simulator: Simulator
    ): SetRunningSimulationCarUseCase {
        return SetRunningSimulationCarUseCase(runningSimulationRepository, simulator)
    }

    @Provides
    @FragmentScope
    fun provideCarDetailsPresenter(): CarDetailsContract.PresenterInterface {
        return CarDetailsPresenter()
    }

}