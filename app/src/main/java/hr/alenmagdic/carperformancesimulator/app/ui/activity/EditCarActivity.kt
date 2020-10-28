package hr.alenmagdic.carperformancesimulator.app.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import hr.alenmagdic.carperformancesimulator.app.CarPerformanceSimulatorApplication
import hr.alenmagdic.carperformancesimulator.app.R
import hr.alenmagdic.carperformancesimulator.app.contracts.EditCarContract
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component.DaggerCarComponent
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.CarModule
import hr.alenmagdic.carperformancesimulator.app.model.CarModel
import hr.alenmagdic.carperformancesimulator.app.ui.fragment.CarDetailsFragment
import javax.inject.Inject

class EditCarActivity : AppCompatActivity(), EditCarContract.ViewInterface {

    @Inject
    lateinit var presenter: EditCarContract.PresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_car)
        injectDependencies()

        val carId = intent.getLongExtra(EXTRA_CAR_ID, 0)

        presenter.setView(this)
        presenter.initialize(carId)
    }

    private fun injectDependencies() {
        DaggerCarComponent.builder()
            .appComponent((application as CarPerformanceSimulatorApplication).appComponent)
            .carModule(CarModule())
            .build()
            .inject(this)
    }

    override fun displayCarData(car: CarModel) {
        val carDetailsFragment =
            supportFragmentManager.findFragmentById(R.id.container_car_details) as CarDetailsFragment?

        if (carDetailsFragment == null || carDetailsFragment.getCarModelArgument()?.id != car.id) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container_car_details,
                CarDetailsFragment.newInstance(car)
            ).commit()
        }
    }

    override fun displayInvalidCarDetailsMessage() {
        AlertDialog.Builder(this)
            .setMessage(R.string.message_save_failed_invalid_car_details)
            .setTitle(R.string.title_save_car_failed)
            .setPositiveButton(R.string.dialog_button_ok) { _, _ -> }
            .show()
    }

    override fun displayTitleEditCar() {
        title = getString(R.string.title_edit_car)
    }

    override fun displayTitleNewCar() {
        title = getString(R.string.title_new_car)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_car, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_car -> {
                presenter.onSaveClick(getCarModelFromInput())
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    companion object {
        private const val EXTRA_CAR_ID = "EXTRA_CAR_ID"

        fun startActivity(context: Context, carId: Long = 0L) {
            val intent = Intent(context, EditCarActivity::class.java)
            intent.putExtra(EXTRA_CAR_ID, carId)

            context.startActivity(intent)
        }

    }

}