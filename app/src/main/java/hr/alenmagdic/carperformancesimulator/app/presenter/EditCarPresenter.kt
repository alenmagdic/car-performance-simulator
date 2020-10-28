package hr.alenmagdic.carperformancesimulator.app.presenter

import hr.alenmagdic.carperformancesimulator.app.contracts.EditCarContract
import hr.alenmagdic.carperformancesimulator.app.model.CarModel
import hr.alenmagdic.carperformancesimulator.app.model.mapper.CarMapper
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.GetCarDetailsUseCase
import hr.alenmagdic.carperformancesimulator.domain.usecase.cars.SaveCarUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EditCarPresenter(
    private val getCarDetailsUseCase: GetCarDetailsUseCase,
    private val saveCarUseCase: SaveCarUseCase,
    private val carMapper: CarMapper
) : EditCarContract.PresenterInterface {

    private lateinit var view: EditCarContract.ViewInterface
    private val disposables = CompositeDisposable()

    override fun setView(view: EditCarContract.ViewInterface) {
        this.view = view
    }

    override fun initialize(carId: Long) {
        if (carId != 0L) {
            view.displayTitleEditCar()
            getCarDetailsUseCase.execute(carId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { car ->
                    view.displayCarData(carMapper.toCarModel(car))
                }.also { disposables.add(it) }
        } else {
            view.displayTitleNewCar()
            view.displayCarData(CarModel())
        }
    }

    override fun onSaveClick(carModel: CarModel) {
        if (!carModel.isModelValid()) {
            view.displayInvalidCarDetailsMessage()
            return
        }

        val car = carMapper.toCar(carModel)
        if (!car.isValid()) {
            view.displayInvalidCarDetailsMessage()
            return
        }

        saveCarUseCase.execute(car)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.finish()
            }.also { disposables.add(it) }
    }

    override fun destroy() {
        disposables.dispose()
    }

}