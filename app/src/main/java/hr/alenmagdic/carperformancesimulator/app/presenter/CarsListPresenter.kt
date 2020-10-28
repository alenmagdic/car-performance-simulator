package hr.alenmagdic.carperformancesimulator.app.presenter

import hr.alenmagdic.carperformancesimulator.app.contracts.CarsListContract
import hr.alenmagdic.carperformancesimulator.app.model.CarModel
import hr.alenmagdic.carperformancesimulator.app.model.mapper.CarMapper
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.GetCarsUseCase
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.RemoveCarUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CarsListPresenter(
    private val getCarsUseCase: GetCarsUseCase,
    private val removeCarUseCase: RemoveCarUseCase,
    private val carMapper: CarMapper
) : CarsListContract.PresenterInterface {
    private lateinit var view: CarsListContract.ViewInterface
    private val disposables = CompositeDisposable()

    override fun setView(view: CarsListContract.ViewInterface) {
        this.view = view
    }

    override fun onAddCarClick() {
        view.displayCarDetails(CarModel())
    }

    override fun onCarClick(car: CarModel) {
        view.displayCarDetails(car)
    }

    override fun onRemoveCarClick(car: CarModel) {
        removeCarUseCase.execute(carMapper.toCar(car))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.hideCar(car)
            }.also { disposables.add(it) }
    }

    override fun onViewDisplay() {
        getCarsUseCase.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { cars ->
                val carModels = cars.map { carMapper.toCarModel(it) }
                view.displayCars(carModels)
            }.also { disposables.add(it) }
    }

    override fun destroy() {
        disposables.dispose()
    }

}