package hr.alenmagdic.carperformancesimulator.app.contracts

import hr.alenmagdic.carperformancesimulator.app.model.AnalogRpmMeterModel
import hr.alenmagdic.carperformancesimulator.app.model.AnalogSpeedometerModel

class SimulatorContract {
    interface PresenterInterface {
        fun setView(view: ViewInterface)
        fun onThrottleInputChanged(throttleInput: Double)
        fun onBrakeInputChanged(brakeInput: Double)
        fun onUpshiftButtonPressed()
        fun onDownshiftButtonPressed()
        fun onShowCarsClick()
        fun onCarSettingsClick()
        fun onAddCarClick()

        fun onViewStart()
        fun onViewStop()
    }

    interface ViewInterface {
        fun displayCarState(carState: hr.alenmagdic.carperformancesimulator.domain.model.CarState)
        fun displayCars()
        fun displayCarSpeedometer(analogSpeedometerModel: AnalogSpeedometerModel)
        fun displayCarRpmMeter(analogRpmMeterModel: AnalogRpmMeterModel)
        fun displayCarSettings()
        fun displayNoCarsInterface()
        fun displayAddNewCarInterface()
        fun hideNoCarsInterface()

        fun displaySimulatorInterface()
        fun hideSimulatorInterface()
    }
}