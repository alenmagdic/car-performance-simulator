package hr.alenmagdic.carperformancesimulator.app.presenter

import hr.alenmagdic.carperformancesimulator.app.contracts.SimulatorContract
import hr.alenmagdic.carperformancesimulator.app.model.mapper.CarMapper
import hr.alenmagdic.carperformancesimulator.domain.Simulator
import hr.alenmagdic.carperformancesimulator.domain.model.*
import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.GetRunningSimulationCarUseCase
import hr.alenmagdic.carperformancesimulator.domain.usecase.simulation.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SimulatorPresenter(
    private val setThrottleInputUseCase: SetThrottleInputUseCase,
    private val setBrakeInputUseCase: SetBrakeInputUseCase,
    private val upshiftUseCase: UpshiftUseCase,
    private val downshiftUseCase: DownshiftUseCase,
    private val startSimulationUseCase: StartSimulationUseCase,
    private val stopSimulationUseCase: StopSimulationUseCase,
    private val getRunningSimulationCarUseCase: GetRunningSimulationCarUseCase,
    private val carMapper: CarMapper
) : SimulatorContract.PresenterInterface {
    private lateinit var view: SimulatorContract.ViewInterface

    private var simulationDisposables = CompositeDisposable()

    override fun setView(view: SimulatorContract.ViewInterface) {
        this.view = view
    }

    override fun onThrottleInputChanged(throttleInput: Double) {
        setThrottleInputUseCase.execute(throttleInput)
    }

    override fun onBrakeInputChanged(brakeInput: Double) {
        setBrakeInputUseCase.execute(brakeInput)
    }

    override fun onUpshiftButtonPressed() {
        upshiftUseCase.execute()
    }

    override fun onDownshiftButtonPressed() {
        downshiftUseCase.execute()
    }

    override fun onShowCarsClick() {
        view.displayCars()
    }

    override fun onCarSettingsClick() {
        view.displayCarSettings()
    }

    override fun onAddCarClick() {
        view.displayAddNewCarInterface()
    }

    override fun onViewStart() {

        simulationDisposables = CompositeDisposable()

        startSimulationUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ simulationObservable ->
                getRunningSimulationCarUseCase.execute()?.let { car ->
                    val carModel = carMapper.toCarModel(car)
                    view.displayCarSpeedometer(carModel.analogSpeedometer)
                    view.displayCarRpmMeter(carModel.analogRpmMeter)
                }

                view.hideNoCarsInterface()
                view.displaySimulatorInterface()

                simulationObservable
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { view.displayCarState(it) }
                    .also { simulationDisposables.add(it) }
            }, {
                view.hideSimulatorInterface()
                view.displayNoCarsInterface()
            }).also { simulationDisposables.add(it) }

    }

    override fun onViewStop() {
        stopSimulationUseCase.execute()
        simulationDisposables.dispose()
    }

}