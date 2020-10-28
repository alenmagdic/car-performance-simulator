package hr.alenmagdic.carperformancesimulator.app.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import hr.alenmagdic.carperformancesimulator.app.CarPerformanceSimulatorApplication
import hr.alenmagdic.carperformancesimulator.app.R
import hr.alenmagdic.carperformancesimulator.app.adapter.GearRatiosAdapter
import hr.alenmagdic.carperformancesimulator.app.adapter.PowerAtRpmAdapter
import hr.alenmagdic.carperformancesimulator.app.contracts.CarDetailsContract
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component.DaggerCarComponent
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.CarModule
import hr.alenmagdic.carperformancesimulator.app.model.*
import hr.alenmagdic.carperformancesimulator.app.ui.*
import kotlinx.android.synthetic.main.fragment_car_details.*
import kotlinx.android.synthetic.main.layout_car_speed_and_rpm.*
import javax.inject.Inject

class CarDetailsFragment : Fragment(), CarDetailsContract.ViewInterface {

    @Inject
    lateinit var carDetailsPresenter: CarDetailsContract.PresenterInterface

    private lateinit var gearRatiosAdapter: GearRatiosAdapter
    private lateinit var powerAtRpmAdapter: PowerAtRpmAdapter

    private var displayedCarModel: CarModel? = null

    fun getCarModelArgument() = arguments?.getParcelable<CarModel>(ARGUMENT_CAR)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    private fun injectDependencies() {
        activity?.application?.let { app ->
            DaggerCarComponent.builder()
                .appComponent((app as CarPerformanceSimulatorApplication).appComponent)
                .carModule(CarModule())
                .build()
                .inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        carDetailsPresenter.setView(this)

        val savedStateGearRatios = savedInstanceState?.getParcelableArray(INSTANCE_STATE_GEAR_RATIOS)
                ?.asList()?.map { it as GearRatioModel }
        val savedStatePowerAtRpmList = savedInstanceState?.getParcelableArray(INSTANCE_STATE_POWER_AT_RPM_LIST)
                ?.asList()?.map { it as PowerAtRpmModel }
        initViews(savedStateGearRatios, savedStatePowerAtRpmList)

        if (savedInstanceState == null) {
            val car = getCarModelArgument()?.let { CarModel(it) }
            car?.let { carDetailsPresenter.initialize(it) }
        }
    }

    private fun initViews(
        gearRatios: List<GearRatioModel>?,
        powerAtRpmList: List<PowerAtRpmModel>?
    ) {
        gearRatiosAdapter = GearRatiosAdapter()
        list_gear_sizes.adapter = gearRatiosAdapter
        list_gear_sizes.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        gearRatios?.let { gearRatiosAdapter.setGearRatios(it) }

        powerAtRpmAdapter = PowerAtRpmAdapter()
        list_power_at_rpm.adapter = powerAtRpmAdapter
        list_power_at_rpm.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        powerAtRpmList?.let { powerAtRpmAdapter.setPowerAtRpmList(it) }

        fab_add_gear.setOnClickListener { carDetailsPresenter.onAddGearRatioClick(gearRatiosAdapter.itemCount) }
        fab_remove_gear.setOnClickListener { carDetailsPresenter.onRemoveGearRatioClick() }

        fab_add_power_at_rpm.setOnClickListener { carDetailsPresenter.onAddPowerAtRpmClick() }
        fab_remove_power_at_rpm.setOnClickListener { carDetailsPresenter.onRemovePowerAtRpmClick() }

        handleInputTextChange(input_max_speedometer_speed) { inputValue ->
            speedometer_preview.maxValue = inputValue
        }
        handleInputTextChange(input_speedometer_segments_count) { inputValue ->
            speedometer_preview.segmentsNumber = inputValue
        }
        handleInputTextChange(input_speedometer_labeled_marks_step) { inputValue ->
            speedometer_preview.labeledMarksStep = inputValue
        }
        handleInputTextChange(input_max_rpm_meter_value) { inputValue ->
            rpm_meter_preview.maxValue = inputValue
        }
        handleInputTextChange(input_rpm_meter_segments_count) { inputValue ->
            rpm_meter_preview.segmentsNumber = inputValue
        }
        handleInputTextChange(input_rpm_meter_labeled_marks_step) { inputValue ->
            rpm_meter_preview.labeledMarksStep = inputValue
        }
        handleInputTextChange(input_rpm_meter_critical_rpm_threshold) { inputValue ->
            rpm_meter_preview.criticallyHighRpmThreshold = inputValue
        }
        handleInputTextChange(input_rpm_meter_value_label_multiplicator) { inputValue ->
            rpm_meter_preview.rpmLabelMultiplicator = inputValue
        }
    }

    private fun handleInputTextChange(inputView: EditText, valueAssigner: ((Int) -> Unit)) {
        inputView.addTextChangedListener {
            it.toString().toIntOrNull()?.let { value ->
                if (value > 0) {
                    valueAssigner(value)
                }
            }
        }
    }

    override fun displayCarData(car: CarModel) {
        input_name.setText(car.name)
        input_weight.setText(car.weight.toStringOrEmpty())
        input_drag_coeff.setText(car.dragCoefficient.toStringOrEmpty())
        input_front_area.setText(car.frontArea.toStringOrEmpty())
        input_tire_pressure.setText(car.tirePressure.toStringOrEmpty())
        input_brake_force.setText(car.brakeForce.toStringOrEmpty())
        input_gear_change_duration.setText(car.transmission.gearChangeLengthMilis.toStringOrEmpty())
        input_max_rpm.setText(car.engine.maxRpm.toStringOrEmpty())
        input_idle_rpm.setText(car.engine.idleRpm.toStringOrEmpty())
        input_compression_energy.setText(car.engine.compressionEnergy.toStringOrEmpty())
        input_max_speedometer_speed.setText(car.analogSpeedometer.maxSpeed.toStringOrEmpty())
        input_speedometer_segments_count.setText(car.analogSpeedometer.segmentsCount.toStringOrEmpty())
        input_speedometer_labeled_marks_step.setText(car.analogSpeedometer.labeledMarksStep.toStringOrEmpty())
        input_max_rpm_meter_value.setText(car.analogRpmMeter.maxRpm.toStringOrEmpty())
        input_rpm_meter_segments_count.setText(car.analogRpmMeter.segmentsCount.toStringOrEmpty())
        input_rpm_meter_labeled_marks_step.setText(car.analogRpmMeter.labeledMarksStep.toStringOrEmpty())
        input_rpm_meter_critical_rpm_threshold.setText(car.analogRpmMeter.criticalRpmThreshold.toStringOrEmpty())
        input_rpm_meter_value_label_multiplicator.setText(car.analogRpmMeter.valueLabelMultiplicator.toStringOrEmpty())

        gearRatiosAdapter.setGearRatios(car.transmission.gearRatios)
        powerAtRpmAdapter.setPowerAtRpmList(car.engine.power)

        displayedCarModel = car
    }

    override fun addGearRatioInput(gearRatio: GearRatioModel) {
        gearRatiosAdapter.addGearRatio(gearRatio)
    }

    override fun addPowerAtRpmInput(powerAtRpm: PowerAtRpmModel) {
        powerAtRpmAdapter.addPowerAtRpm(powerAtRpm)
    }

    override fun removeLastGearRatioInput() {
        if (gearRatiosAdapter.itemCount > 0) {
            gearRatiosAdapter.removeGearRatio(gearRatiosAdapter.itemCount - 1)
        }
    }

    override fun removeLastPowerAtRpmInput() {
        if (powerAtRpmAdapter.itemCount > 0) {
            powerAtRpmAdapter.removePowerAtRpmRatio(powerAtRpmAdapter.itemCount - 1)
        }
    }

    fun getCarModelFromInput(): CarModel {
        return CarModel(
            id = displayedCarModel?.id ?: 0L,
            name = input_name.getTextAsString(),
            weight = input_weight.getTextAsDouble(),
            dragCoefficient = input_drag_coeff.getTextAsDouble(),
            frontArea = input_front_area.getTextAsDouble(),
            tirePressure = input_tire_pressure.getTextAsDouble(),
            brakeForce = input_brake_force.getTextAsInt(),

            transmission = TransmissionModel(
                gearChangeLengthMilis = input_gear_change_duration.getTextAsLong(),
                gearRatios = gearRatiosAdapter.getGearRatios()
            ),

            engine = EngineModel(
                maxRpm = input_max_rpm.getTextAsDouble(),
                idleRpm = input_idle_rpm.getTextAsDouble(),
                compressionEnergy = input_compression_energy.getTextAsDouble(),
                power = powerAtRpmAdapter.getPowerAtRpmList()
            ),

            analogSpeedometer = AnalogSpeedometerModel(
                maxSpeed = input_max_speedometer_speed.getTextAsInt(),
                segmentsCount = input_speedometer_segments_count.getTextAsInt(),
                labeledMarksStep = input_speedometer_labeled_marks_step.getTextAsInt()
            ),

            analogRpmMeter = AnalogRpmMeterModel(
                maxRpm = input_max_rpm_meter_value.getTextAsInt(),
                segmentsCount = input_rpm_meter_segments_count.getTextAsInt(),
                labeledMarksStep = input_rpm_meter_labeled_marks_step.getTextAsInt(),
                criticalRpmThreshold = input_rpm_meter_critical_rpm_threshold.getTextAsInt(),
                valueLabelMultiplicator = input_rpm_meter_value_label_multiplicator.getTextAsInt()
            )
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArray(
            INSTANCE_STATE_GEAR_RATIOS,
            gearRatiosAdapter.getGearRatios().toTypedArray()
        )

        outState.putParcelableArray(
            INSTANCE_STATE_POWER_AT_RPM_LIST,
            powerAtRpmAdapter.getPowerAtRpmList().toTypedArray()
        )
    }

    companion object {
        private const val ARGUMENT_CAR = "ARGUMENT_CAR"
        private const val INSTANCE_STATE_GEAR_RATIOS = "INSTANCE_STATE_GEAR_RATIOS"
        private const val INSTANCE_STATE_POWER_AT_RPM_LIST = "INSTANCE_STATE_POWER_AT_RPM_LIST"

        @JvmStatic
        fun newInstance(car: CarModel) =
            CarDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARGUMENT_CAR, car)
                }
            }
    }
}