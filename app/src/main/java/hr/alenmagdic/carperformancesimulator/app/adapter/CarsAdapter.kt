package hr.alenmagdic.carperformancesimulator.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.alenmagdic.carperformancesimulator.app.R
import hr.alenmagdic.carperformancesimulator.app.model.CarModel

class CarsAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<CarsAdapter.ViewHolder>() {

    private val cars: MutableList<CarModel> = mutableListOf()

    fun setCars(cars: List<CarModel>) {
        this.cars.clear()
        this.cars.addAll(cars)
        notifyDataSetChanged()
    }

    fun removeCar(carModel: CarModel) {
        val carIndex = cars.indexOf(carModel)
        cars.removeAt(carIndex)
        notifyItemRemoved(carIndex)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = cars[position]
        val maxPowerAtRpm = car.engine.getMaxPower()

        holder.apply {
            carName.text = car.name
            maxPower.text = maxPowerAtRpm?.power.toString()
            ratedSpeed.text = maxPowerAtRpm?.rpm.toString()
            gearsCount.text = car.transmission.numberOfGears().toString()
            weight.text = car.weight.toString()

            itemView.setOnClickListener { itemClickListener.onCarClick(car) }
            removeCar.setOnClickListener { itemClickListener.onRemoveCarClick(car) }
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carName: TextView = view.findViewById(R.id.text_name)
        val maxPower: TextView = view.findViewById(R.id.text_max_power_value)
        val ratedSpeed: TextView = view.findViewById(R.id.text_rated_speed_value)
        val gearsCount: TextView = view.findViewById(R.id.text_gears_count_value)
        val weight: TextView = view.findViewById(R.id.text_weight_value)
        val removeCar: ImageButton = view.findViewById(R.id.button_remove)
    }

    interface ItemClickListener {
        fun onCarClick(car: CarModel)
        fun onRemoveCarClick(car: CarModel)
    }
}