package hr.alenmagdic.carperformancesimulator.app.model.mapper

import hr.alenmagdic.carperformancesimulator.app.model.CarModel
import hr.alenmagdic.carperformancesimulator.domain.model.Car
import javax.inject.Inject

class CarMapper @Inject constructor(
    private val engineMapper: EngineMapper,
    private val transmissionMapper: TransmissionMapper,
    private val analogRpmMeterMapper: AnalogRpmMeterMapper,
    private val analogSpeedometerMapper: AnalogSpeedometerMapper
) {

    fun toCar(carModel: CarModel): Car {
        return Car(
            carModel.id,
            carModel.name,
            transmissionMapper.toTransmission(carModel.transmission),
            engineMapper.toEngine(carModel.engine),
            carModel.weight!!,
            carModel.dragCoefficient!!,
            carModel.frontArea!!,
            carModel.tirePressure!!,
            carModel.brakeForce!!,
            analogSpeedometerMapper.toAnalogSpeedometer(carModel.analogSpeedometer),
            analogRpmMeterMapper.toAnalogRpmMeter(carModel.analogRpmMeter)
        )
    }

    fun toCarModel(car: Car): CarModel {
        return CarModel(
            car.id,
            car.name,
            transmissionMapper.toTransmissionModel(car.transmission),
            engineMapper.toEngineModel(car.engine),
            car.weight,
            car.dragCoefficient,
            car.frontArea,
            car.tirePressure,
            car.brakeForce,
            analogSpeedometerMapper.toAnalogSpeedometerModel(car.analogSpeedometer),
            analogRpmMeterMapper.toAnalogRpmMeterModel(car.analogRpmMeter)
        )
    }
}