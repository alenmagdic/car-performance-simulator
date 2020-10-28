package hr.alenmagdic.carperformancesimulator.app.presenter

import hr.alenmagdic.carperformancesimulator.app.contracts.SimulationCarSettingsContract
import hr.alenmagdic.carperformancesimulator.app.model.CarModel
import hr.alenmagdic.carperformancesimulator.app.model.mapper.CarMapper
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.GetCarsUseCase
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.GetRunningSimulationCarUseCase
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.SetRunningSimulationCarUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SimulationCarSettingsPresenter(
    private val getRunningSimulationCarUseCase: GetRunningSimulationCarUseCase,
    private val setRunningSimulationCarUseCase: SetRunningSimulationCarUseCase,
    private val carMapper: CarMapper,
    private val getCarsUseCase: GetCarsUseCase
) : SimulationCarSettingsContract.PresenterInterface {
    private lateinit var view: SimulationCarSettingsContract.ViewInterface

    private var disposables = CompositeDisposable()

    override fun setView(view: SimulationCarSettingsContract.ViewInterface) {
        this.view = view
    }

    override fun reinitialize(previouslySelectedCarIndex: Int) {
        initialize(previouslySelectedCarIndex)
    }

    override fun initialize() {
        initialize(null)
    }

    private fun initialize(previouslySelectedCarIndex: Int?) {
        val currentSimulationCar = getRunningSimulationCarUseCase.execute()

        getCarsUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { cars ->
                val carModels = cars.map { carMapper.toCarModel(it) }.toMutableList()
                val currentSimCarModel = currentSimulationCar?.let { carMapper.toCarModel(it) }

                val currentSimCarModelIndex = carModels.indexOf(currentSimCarModel)
                var initialSelectedCarIndex = previouslySelectedCarIndex ?: currentSimCarModelIndex
                if (currentSimCarModelIndex == -1) {
                    currentSimCarModel?.let { carModels.add(CUSTOM_CAR_SELECTOR_INDEX, it) }

                    if (previouslySelectedCarIndex == null) {
                        initialSelectedCarIndex = CUSTOM_CAR_SELECTOR_INDEX
                    }
                }

                val customCarModelIndex =
                    if (currentSimCarModelIndex == -1) {
                        CUSTOM_CAR_SELECTOR_INDEX
                    } else {
                        null
                    }

                view.setupCarSelector(carModels, customCarModelIndex, initialSelectedCarIndex)
            }.also { disposables.add(it) }
    }

    override fun destroy() {
        disposables.dispose()
    }

    override fun onApplyChangesClick(carModel: CarModel) {
        if (!carModel.isModelValid()) {
            view.displayInvalidCarDetailsMessage()
            return
        }

        val car = carMapper.toCar(carModel)
        if (!car.isValid()) {
            view.displayInvalidCarDetailsMessage()
            return
        }

        setRunningSimulationCarUseCase.execute(car)

        view.returnToSimulation()
    }

    override fun onCarSelected(car: CarModel) {
        view.displayCarDetails(car)
    }

    companion object {
        private const val CUSTOM_CAR_SELECTOR_INDEX = 0
    }

}