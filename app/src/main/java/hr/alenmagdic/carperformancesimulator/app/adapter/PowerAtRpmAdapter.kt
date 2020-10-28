package hr.alenmagdic.carperformancesimulator.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import hr.alenmagdic.carperformancesimulator.app.R
import hr.alenmagdic.carperformancesimulator.app.model.PowerAtRpmModel

class PowerAtRpmAdapter : RecyclerView.Adapter<PowerAtRpmAdapter.ViewHolder>() {
    private val powerAtRpmList = mutableListOf<PowerAtRpmModel>()

    fun setPowerAtRpmList(powerAtRpmList: List<PowerAtRpmModel>) {
        this.powerAtRpmList.clear()
        this.powerAtRpmList.addAll(powerAtRpmList)
        notifyDataSetChanged()
    }

    fun getPowerAtRpmList(): List<PowerAtRpmModel> {
        return this.powerAtRpmList.toList()
    }

    fun addPowerAtRpm(powerAtRpm: PowerAtRpmModel) {
        powerAtRpmList.add(powerAtRpm)
        notifyItemInserted(powerAtRpmList.lastIndex)
    }

    fun removePowerAtRpmRatio(index: Int) {
        powerAtRpmList.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
        return powerAtRpmList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_power_at_rpm, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val powerAtRpm = powerAtRpmList[position]

        holder.powerAtRpm.setText(powerAtRpm.power?.toString() ?: "")
        holder.rpm.setText(powerAtRpm.rpm?.toString() ?: "")

        holder.powerAtRpm.addTextChangedListener {
            powerAtRpm.power = it.toString().toDoubleOrNull()
        }
        holder.rpm.addTextChangedListener {
            powerAtRpm.rpm = it.toString().toDoubleOrNull()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rpm: EditText = view.findViewById(R.id.input_rpm)
        val powerAtRpm: EditText = view.findViewById(R.id.input_power_at_rpm)
    }
}