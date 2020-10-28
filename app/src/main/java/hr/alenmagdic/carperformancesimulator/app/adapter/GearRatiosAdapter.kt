package hr.alenmagdic.carperformancesimulator.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import hr.alenmagdic.carperformancesimulator.app.R
import hr.alenmagdic.carperformancesimulator.app.model.GearRatioModel

class GearRatiosAdapter : RecyclerView.Adapter<GearRatiosAdapter.ViewHolder>() {
    private val gearRatios = mutableListOf<GearRatioModel>()

    fun setGearRatios(gearRatios: List<GearRatioModel>) {
        this.gearRatios.clear()
        this.gearRatios.addAll(gearRatios)
        notifyDataSetChanged()
    }

    fun getGearRatios(): List<GearRatioModel> {
        return this.gearRatios.toList()
    }

    fun addGearRatio(gearRatio: GearRatioModel) {
        gearRatios.add(gearRatio)
        notifyItemInserted(gearRatios.lastIndex)
    }

    fun removeGearRatio(index: Int) {
        gearRatios.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
        return gearRatios.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gear_size, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gearRatio = gearRatios[position]

        holder.apply {
            gearNumber.text = gearRatio.gearNumber.toString()
            rpmAtSpeed.setText(gearRatio.rpmAtSpeed?.toString() ?: "")
            speed.setText(gearRatio.speedKmH?.toString() ?: "")

            rpmAtSpeed.addTextChangedListener {
                gearRatio.rpmAtSpeed = it.toString().toDoubleOrNull()
            }
            speed.addTextChangedListener {
                gearRatio.speedKmH = it.toString().toDoubleOrNull()
            }
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val gearNumber: TextView = view.findViewById(R.id.text_gear_number)
        val speed: EditText = view.findViewById(R.id.input_speed)
        val rpmAtSpeed: EditText = view.findViewById(R.id.input_rpm_at_speed)


    }
}