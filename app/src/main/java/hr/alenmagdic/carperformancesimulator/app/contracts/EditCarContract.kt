package hr.alenmagdic.carperformancesimulator.app.contracts

import hr.alenmagdic.carperformancesimulator.app.model.CarModel

class EditCarContract {
    interface PresenterInterface {
        fun setView(view: ViewInterface)

        fun onSaveClick(carModel: CarModel)

        fun initialize(carId: Long)
        fun destroy()
    }

    interface ViewInterface {
        fun displayCarData(car: CarModel)
        fun displayInvalidCarDetailsMessage()
        fun displayTitleEditCar()
        fun displayTitleNewCar()
        fun finish()
    }
}