package hr.alenmagdic.carperformancesimulator.app.contracts

import hr.alenmagdic.carperformancesimulator.app.model.CarModel

class CarsListContract {
    interface PresenterInterface {
        fun setView(view: ViewInterface)

        fun onAddCarClick()
        fun onCarClick(car: CarModel)
        fun onRemoveCarClick(car: CarModel)
        fun onViewDisplay()

        fun destroy()
    }

    interface ViewInterface {
        fun displayCarDetails(car: CarModel)
        fun displayCars(cars: List<CarModel>)
        fun hideCar(carModel: CarModel)
    }
}