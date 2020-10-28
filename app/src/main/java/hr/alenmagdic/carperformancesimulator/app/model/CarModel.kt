package hr.alenmagdic.carperformancesimulator.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CarModel(
    var id: Long = 0,
    var name: String = "",
    var transmission: TransmissionModel = TransmissionModel(),
    var engine: EngineModel = EngineModel(),
    var weight: Double? = null,
    var dragCoefficient: Double? = null,
    var frontArea: Double? = null,
    var tirePressure: Double? = null,
    var brakeForce: Int? = null,
    var analogSpeedometer: AnalogSpeedometerModel = AnalogSpeedometerModel(),
    var analogRpmMeter: AnalogRpmMeterModel = AnalogRpmMeterModel()
) : Parcelable {

    constructor(carModel: CarModel) : this(
        carModel.id,
        carModel.name,
        TransmissionModel(carModel.transmission),
        EngineModel(carModel.engine),
        carModel.weight,
        carModel.dragCoefficient,
        carModel.frontArea,
        carModel.tirePressure,
        carModel.brakeForce,
        AnalogSpeedometerModel(carModel.analogSpeedometer),
        AnalogRpmMeterModel(carModel.analogRpmMeter)
    )

    fun isModelValid(): Boolean {
        return transmission.isModelValid() &&
                engine.isModelValid() &&
                weight != null &&
                dragCoefficient != null &&
                frontArea != null &&
                tirePressure != null &&
                brakeForce != null &&
                analogRpmMeter.isValid() &&
                analogSpeedometer.isValid()
    }

}