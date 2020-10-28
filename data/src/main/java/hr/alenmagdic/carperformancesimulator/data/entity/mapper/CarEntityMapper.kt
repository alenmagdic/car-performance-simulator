package hr.alenmagdic.carperformancesimulator.data.entity.mapper

import hr.alenmagdic.carperformancesimulator.data.entity.CarEntity
import hr.alenmagdic.carperformancesimulator.data.entity.GearRatioEntity
import hr.alenmagdic.carperformancesimulator.data.entity.PowerAtRpmEntity
import hr.alenmagdic.carperformancesimulator.domain.model.*

class CarEntityMapper(
    private val gearRatioEntityMapper: GearRatioEntityMapper,
    private val powerAtRpmEntityMapper: PowerAtRpmEntityMapper
) {

    fun toCarEntity(car: Car): CarEntity {
        return CarEntity(
            car.id,
            car.name,
            car.weight,
            car.dragCoefficient,
            car.frontArea,
            car.tirePressure,
            car.brakeForce,
            car.transmission.gearChangeLengthMilis,
            car.engine.maxRpm,
            car.engine.idleRpm,
            car.engine.compressionEnergy,
            car.analogSpeedometer.maxSpeed,
            car.analogSpeedometer.segmentsCount,
            car.analogSpeedometer.labeledMarksStep,
            car.analogRpmMeter.maxRpm,
            car.analogRpmMeter.segmentsCount,
            car.analogRpmMeter.labeledMarksStep,
            car.analogRpmMeter.criticalRpmThreshold,
            car.analogRpmMeter.valueLabelMultiplicator
        )
    }

    fun toCar(
        carEntity: CarEntity,
        gearRatios: List<GearRatioEntity>,
        powerAtRpmList: List<PowerAtRpmEntity>
    ): Car {
        val transmission = Transmission(
            carEntity.gearChangeLengthMilis,
            gearRatios.map { gearRatioEntityMapper.toGearRatio(it) }
        )

        val engine = Engine(
            carEntity.maxRpm,
            carEntity.idleRpm,
            carEntity.compressionEnergy,
            powerAtRpmList.map { powerAtRpmEntityMapper.toPowerAtRpm(it) }
        )

        val analogSpeedometer = AnalogSpeedometer(
            carEntity.maxSpeedometerSpeed,
            carEntity.speedometerSegmentsCount,
            carEntity.speedometerLabeledMarksStep
        )

        val analogRpmMeter = AnalogRpmMeter(
            carEntity.maxRpmMeterValue,
            carEntity.rpmMeterSegmentsCount,
            carEntity.rpmMeterLabeledMarksStep,
            carEntity.rpmMeterCriticalRpmThreshold,
            carEntity.rpmMeterValueLabelMultiplicator
        )

        return Car(
            carEntity.id,
            carEntity.name,
            transmission,
            engine,
            carEntity.weight,
            carEntity.dragCoefficient,
            carEntity.frontArea,
            carEntity.tirePressure,
            carEntity.brakeForce,
            analogSpeedometer,
            analogRpmMeter
        )
    }

}