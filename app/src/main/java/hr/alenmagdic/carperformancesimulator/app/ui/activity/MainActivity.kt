package hr.alenmagdic.carperformancesimulator.app.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import hr.alenmagdic.carperformancesimulator.app.CarPerformanceSimulatorApplication
import hr.alenmagdic.carperformancesimulator.app.R
import hr.alenmagdic.carperformancesimulator.app.contracts.SimulatorContract
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component.DaggerSimulatorComponent
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.CarModule
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.SimulatorModule
import hr.alenmagdic.carperformancesimulator.app.model.AnalogRpmMeterModel
import hr.alenmagdic.carperformancesimulator.app.model.AnalogSpeedometerModel
import hr.alenmagdic.carperformancesimulator.app.ui.view.VerticalSliderView
import hr.alenmagdic.carperformancesimulator.domain.model.CarState
import hr.alenmagdic.carperformancesimulator.domain.round
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_car_state.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), SimulatorContract.ViewInterface {
    @Inject
    lateinit var presenter: SimulatorContract.PresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()

        initViews()

        presenter.setView(this)
    }

    private fun injectDependencies() {
        DaggerSimulatorComponent.builder()
            .appComponent((application as CarPerformanceSimulatorApplication).appComponent)
            .simulatorModule(SimulatorModule())
            .carModule(CarModule())
            .build()
            .inject(this)
    }

    private fun initViews() {
        slider_throttle_input.setSliderChangeListener(inputChangeListener)
        slider_brake_input.setSliderChangeListener(inputChangeListener)

        button_upshift.setOnClickListener { presenter.onUpshiftButtonPressed() }
        button_downshift.setOnClickListener { presenter.onDownshiftButtonPressed() }

        button_car_settings.setOnClickListener {
            presenter.onCarSettingsClick()
        }

        speedometer_mini?.valueMarkColor =
            ContextCompat.getColor(this, R.color.miniSpeedometerColor)
        rpm_meter_mini?.valueMarkColor = ContextCompat.getColor(this, R.color.miniRpmMeterColor)

        switch_car_state?.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                switcher_car_state?.showNext()
            } else {
                switcher_car_state?.showPrevious()
            }
        }
        switcher_car_state?.showNext()

        button_add_car.setOnClickListener {
            presenter.onAddCarClick()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewStop()
    }

    private val inputChangeListener = object : VerticalSliderView.SliderChangeListener {
        val VALUE_SCALE_FACTOR = 100.0

        override fun onSliderChanged(value: Double, slider: VerticalSliderView) {
            val normalisedValue = value / VALUE_SCALE_FACTOR
            when (slider) {
                slider_throttle_input -> presenter.onThrottleInputChanged(normalisedValue)
                slider_brake_input -> presenter.onBrakeInputChanged(normalisedValue)
            }
        }
    }


    override fun displayCarState(carState: CarState) {
        val roundValue: (Double) -> (String) =
            { value -> value.round(CAR_STATE_VALUES_DECIMAL_PLACES).toString() }

        speedometer.value = carState.speedKmH
        rpm_meter.value = carState.rpm
        speedometer_mini?.value = carState.speedKmH
        rpm_meter_mini?.value = carState.rpm
        text_speed_value.text = roundValue(carState.speedKmH)
        text_rpm_value.text = roundValue(carState.rpm)
        text_gear_value.text = carState.gear.toString()
        text_total_distance_value.text = roundValue(carState.totalDistance)
        text_used_power_value.text = roundValue(carState.usedPower)
        text_available_power_value.text = roundValue(carState.availablePower)
        text_used_torque_value.text = roundValue(carState.usedTorque)
        text_available_torque_value.text = roundValue(carState.availableTorque)
        text_kinetic_energy_value.text = roundValue(carState.kineticEnergy)
        text_produced_energy_value.text = roundValue(carState.producedEnergy)
        text_acceleration_value.text = roundValue(carState.acceleration)
        text_roll_resistance_value.text = roundValue(carState.rollResistance)
        text_air_resistance_value.text = roundValue(carState.airResistance)
        text_roll_resistance_power_value.text = roundValue(carState.rollResistancePower)
        text_air_resistance_power_value.text = roundValue(carState.airResistancePower)
        text_acceleration_power_value.text = roundValue(carState.accelerationPower)
    }

    override fun displayCarSpeedometer(analogSpeedometerModel: AnalogSpeedometerModel) {
        analogSpeedometerModel.maxSpeed?.let { speedometer.maxValue = it }
        analogSpeedometerModel.segmentsCount?.let { speedometer.segmentsNumber = it }
        analogSpeedometerModel.labeledMarksStep?.let { speedometer.labeledMarksStep = it }

        applySpeedometerConfigToMiniSpeedometer()
    }

    private fun applySpeedometerConfigToMiniSpeedometer() {
        speedometer_mini?.apply {
            maxValue = speedometer.maxValue
            segmentsNumber = speedometer.segmentsNumber
            labeledMarksStep = speedometer.labeledMarksStep
        }
    }

    override fun displayCarRpmMeter(analogRpmMeterModel: AnalogRpmMeterModel) {
        analogRpmMeterModel.maxRpm?.let { rpm_meter.maxValue = it }
        analogRpmMeterModel.segmentsCount?.let { rpm_meter.segmentsNumber = it }
        analogRpmMeterModel.labeledMarksStep?.let { rpm_meter.labeledMarksStep = it }
        analogRpmMeterModel.criticalRpmThreshold?.let { rpm_meter.criticallyHighRpmThreshold = it }
        analogRpmMeterModel.valueLabelMultiplicator?.let { rpm_meter.rpmLabelMultiplicator = it }

        applyRpmMeterConfigToMiniRpmMeter()
    }

    private fun applyRpmMeterConfigToMiniRpmMeter() {
        rpm_meter_mini?.apply {
            maxValue = rpm_meter.maxValue
            segmentsNumber = rpm_meter.segmentsNumber
            labeledMarksStep = rpm_meter.labeledMarksStep
            criticallyHighRpmThreshold = rpm_meter.criticallyHighRpmThreshold
            rpmLabelMultiplicator = rpm_meter.rpmLabelMultiplicator
        }
    }

    override fun displayCarSettings() {
        SimulationCarSettingsActivity.startActivity(this)
    }

    override fun displaySimulatorInterface() {
        layout_simulator_user_interface.visibility = View.VISIBLE
    }

    override fun hideSimulatorInterface() {
        layout_simulator_user_interface.visibility = View.INVISIBLE
    }

    override fun displayNoCarsInterface() {
        layout_no_cars_interface.visibility = View.VISIBLE
    }

    override fun hideNoCarsInterface() {
        layout_no_cars_interface.visibility = View.INVISIBLE
    }

    override fun displayAddNewCarInterface() {
        EditCarActivity.startActivity(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_cars -> {
                presenter.onShowCarsClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun displayCars() {
        CarsListActivity.startActivity(this)
    }

    companion object {
        private const val CAR_STATE_VALUES_DECIMAL_PLACES = 1
    }
}
