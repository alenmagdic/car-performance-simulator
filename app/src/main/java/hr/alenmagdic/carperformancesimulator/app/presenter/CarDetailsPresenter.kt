package hr.alenmagdic.carperformancesimulator.app.presenter

import hr.alenmagdic.carperformancesimulator.app.contracts.CarDetailsContract
import hr.alenmagdic.carperformancesimulator.app.model.CarModel
import hr.alenmagdic.carperformancesimulator.app.model.GearRatioModel
import hr.alenmagdic.carperformancesimulator.app.model.PowerAtRpmModel

class CarDetailsPresenter : CarDetailsContract.PresenterInterface {
    private lateinit var view: CarDetailsContract.ViewInterface

    override fun setView(view: CarDetailsContract.ViewInterface) {
        this.view = view
    }

    override fun initialize(car: CarModel) {
        view.displayCarData(car)
    }

    override fun onAddGearRatioClick(gearRatioCount: Int) {
        view.addGearRatioInput(GearRatioModel(gearNumber = gearRatioCount + 1))
    }

    override fun onRemoveGearRatioClick() {
        view.removeLastGearRatioInput()
    }

    override fun onAddPowerAtRpmClick() {
        view.addPowerAtRpmInput(PowerAtRpmModel())
    }

    override fun onRemovePowerAtRpmClick() {
        view.removeLastPowerAtRpmInput()
    }
}