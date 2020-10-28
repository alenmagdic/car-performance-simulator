package hr.alenmagdic.carperformancesimulator.app.contracts

import hr.alenmagdic.carperformancesimulator.app.model.CarModel
import hr.alenmagdic.carperformancesimulator.app.model.GearRatioModel
import hr.alenmagdic.carperformancesimulator.app.model.PowerAtRpmModel

class CarDetailsContract {
    interface PresenterInterface {
        fun setView(view: ViewInterface)

        fun onAddGearRatioClick(gearRatioCount: Int)
        fun onRemoveGearRatioClick()
        fun onAddPowerAtRpmClick()
        fun onRemovePowerAtRpmClick()

        fun initialize(car: CarModel)
    }

    interface ViewInterface {
        fun addGearRatioInput(gearRatio: GearRatioModel)
        fun removeLastGearRatioInput()
        fun addPowerAtRpmInput(powerAtRpm: PowerAtRpmModel)
        fun removeLastPowerAtRpmInput()
        fun displayCarData(car: CarModel)
    }
}