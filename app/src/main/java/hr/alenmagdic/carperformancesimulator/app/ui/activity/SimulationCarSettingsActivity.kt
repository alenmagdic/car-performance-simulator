package hr.alenmagdic.carperformancesimulator.app.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import hr.alenmagdic.carperformancesimulator.app.CarPerformanceSimulatorApplication
import hr.alenmagdic.carperformancesimulator.app.R
import hr.alenmagdic.carperformancesimulator.app.contracts.SimulationCarSettingsContract
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component.DaggerCarComponent
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.CarModule
import hr.alenmagdic.carperformancesimulator.app.model.*
import hr.alenmagdic.carperformancesimulator.app.ui.fragment.CarDetailsFragment
import kotlinx.android.synthetic.main.activity_simulation_car_settings.*
import javax.inject.Inject

class SimulationCarSettingsActivity : AppCompatActivity(),
    SimulationCarSettingsContract.ViewInterface {

    @Inject
    lateinit var simulationCarSettingsPresenter: SimulationCarSettingsContract.PresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulation_car_settings)
        injectDependencies()

        title = getString(R.string.title_simulation_car_settings)

        simulationCarSettingsPresenter.setView(this)

        if (savedInstanceState != null) {
            val selectedCarIndex = savedInstanceState.getInt(INSTANCE_STATE_SELECTED_CAR_INDEX)
            simulationCarSettingsPresenter.reinitialize(selectedCarIndex)
        } else {
            simulationCarSettingsPresenter.initialize()
        }

    }

    private fun injectDependencies() {
        DaggerCarComponent.builder()
            .appComponent((application as CarPerformanceSimulatorApplication).appComponent)
            .carModule(CarModule())
            .build()
            .inject(this)
    }

    override fun setupCarSelector(
        cars: List<CarModel>,
        customModelIndex: Int?,
        initialSelectionIndex: Int
    ) {
        val spinnerOptions = cars.map { it.name }.toMutableList()
        customModelIndex?.let {
            spinnerOptions[it] = getString(R.string.item_custom_car_model, spinnerOptions[it])
        }

        spinner_simulation_car.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerOptions)
        spinner_simulation_car.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    simulationCarSettingsPresenter.onCarSelected(cars[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        spinner_simulation_car.setSelection(initialSelectionIndex)
    }

    override fun displayCarDetails(car: CarModel) {
        val carDetailsFragment =
            supportFragmentManager.findFragmentById(R.id.container_car_details) as CarDetailsFragment?

        if (carDetailsFragment == null || carDetailsFragment.getCarModelArgument() != car) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container_car_details,
                CarDetailsFragment.newInstance(car)
            ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }

    }

    override fun displayInvalidCarDetailsMessage() {
        AlertDialog.Builder(this)
            .setMessage(R.string.message_apply_changes_failed_invalid_car_details)
            .setTitle(R.string.title_apply_changes_failed)
            .setPositiveButton(R.string.dialog_button_ok) { _, _ -> }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_simulation_car_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_apply_changes -> {
                simulationCarSettingsPresenter.onApplyChangesClick(getCarModelFromInput())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getCarModelFromInput(): CarModel {
        val carDetailsFragment = supportFragmentManager.findFragmentById(
            R.id.container_car_details
        ) as CarDetailsFragment

        return carDetailsFragment.getCarModelFromInput()
    }

    override fun returnToSimulation() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        simulationCarSettingsPresenter.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            INSTANCE_STATE_SELECTED_CAR_INDEX,
            spinner_simulation_car.selectedItemPosition
        )
    }

    companion object {
        private const val INSTANCE_STATE_SELECTED_CAR_INDEX = "INSTANCE_STATE_SELECTED_CAR_INDEX"

        fun startActivity(context: Context) {
            val intent = Intent(context, SimulationCarSettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}
