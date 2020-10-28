package hr.alenmagdic.carperformancesimulator.app.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import hr.alenmagdic.carperformancesimulator.app.CarPerformanceSimulatorApplication
import hr.alenmagdic.carperformancesimulator.app.R
import hr.alenmagdic.carperformancesimulator.app.adapter.CarsAdapter
import hr.alenmagdic.carperformancesimulator.app.contracts.CarsListContract
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.component.DaggerCarComponent
import hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module.CarModule
import hr.alenmagdic.carperformancesimulator.app.model.CarModel
import kotlinx.android.synthetic.main.activity_cars_list.*
import javax.inject.Inject

class CarsListActivity : AppCompatActivity(), CarsListContract.ViewInterface,
    CarsAdapter.ItemClickListener {

    @Inject
    lateinit var presenter: CarsListContract.PresenterInterface

    private var carsAdapter = CarsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cars_list)
        injectDependencies()

        title = getString(R.string.title_cars)

        initViews()

        presenter.setView(this)
    }

    private fun initViews() {
        list_cars.adapter = carsAdapter
        list_cars.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        fab_add_car.setOnClickListener {
            presenter.onAddCarClick()
        }

    }

    private fun injectDependencies() {
        DaggerCarComponent.builder()
            .appComponent((application as CarPerformanceSimulatorApplication).appComponent)
            .carModule(CarModule())
            .build()
            .inject(this)
    }

    override fun displayCarDetails(car: CarModel) {
        EditCarActivity.startActivity(this, car.id)
    }

    override fun displayCars(cars: List<CarModel>) {
        carsAdapter.setCars(cars)
    }

    override fun hideCar(carModel: CarModel) {
        carsAdapter.removeCar(carModel)
    }

    override fun onCarClick(car: CarModel) {
        presenter.onCarClick(car)
    }

    override fun onRemoveCarClick(car: CarModel) {
        presenter.onRemoveCarClick(car)
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewDisplay()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    companion object {

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, CarsListActivity::class.java))
        }

    }

}