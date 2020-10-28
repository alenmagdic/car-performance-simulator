package hr.alenmagdic.carperformancesimulator.app.contracts

import hr.alenmagdic.carperformancesimulator.app.model.CarModel

class SimulationCarSettingsContract {
    interface PresenterInterface {
        fun setView(view: ViewInterface)

        fun onApplyChangesClick(carModel: CarModel)
        fun onCarSelected(car: CarModel)

        fun initialize()
        fun reinitialize(previouslySelectedCarIndex: Int)
        fun destroy()
    }

    interface ViewInterface {
        fun displayCarDetails(car: CarModel)
        fun setupCarSelector(cars: List<CarModel>, customModelIndex: Int?, initialSelectionIndex: Int)

        fun returnToSimulation()
        fun displayInvalidCarDetailsMessage()
    }
}