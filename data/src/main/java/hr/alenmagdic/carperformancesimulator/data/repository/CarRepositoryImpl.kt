package hr.alenmagdic.carperformancesimulator.data.repository

import hr.alenmagdic.carperformancesimulator.data.dao.CarDao
import hr.alenmagdic.carperformancesimulator.data.dao.GearRatioDao
import hr.alenmagdic.carperformancesimulator.data.dao.PowerAtRpmDao
import hr.alenmagdic.carperformancesimulator.data.entity.mapper.CarEntityMapper
import hr.alenmagdic.carperformancesimulator.data.entity.mapper.GearRatioEntityMapper
import hr.alenmagdic.carperformancesimulator.data.entity.mapper.PowerAtRpmEntityMapper
import hr.alenmagdic.carperformancesimulator.domain.model.Car
import hr.alenmagdic.carperformancesimulator.domain.repository.CarRepository

class CarRepositoryImpl(
    private val carDao: CarDao,
    private val gearRatioDao: GearRatioDao,
    private val powerAtRpmDao: PowerAtRpmDao,
    private val carEntityMapper: CarEntityMapper,
    private val gearRatioEntityMapper: GearRatioEntityMapper,
    private val powerAtRpmEntityMapper: PowerAtRpmEntityMapper
) : CarRepository {

    override fun save(car: Car) {
        val carEntity = carEntityMapper.toCarEntity(car)
        val gearRatioEntities = car.transmission.gearRatios.map { gearRatioEntityMapper.toGearRatioEntity(it, car.id) }
        val powerAtRpmEntities = car.engine.power.map { powerAtRpmEntityMapper.toPowerAtRpmEntity(it, car.id) }

        val carId = carDao.save(carEntity)

        val oldGearRatioEntities = gearRatioDao.getForCar(carId)
        oldGearRatioEntities.forEach {
            if (!gearRatioEntities.contains(it)) {
                gearRatioDao.delete(it)
            }
        }

        val oldPowerAtRpmEntities = powerAtRpmDao.getForCar(carId)
        oldPowerAtRpmEntities.forEach {
            if (!powerAtRpmEntities.contains(it)) {
                powerAtRpmDao.delete(it)
            }
        }

        gearRatioEntities.forEach {
            it.carId = carId
            gearRatioDao.save(it)
        }
        powerAtRpmEntities.forEach {
            it.carId = carId
            powerAtRpmDao.save(it)
        }
    }

    override fun getById(id: Long): Car {
        val carEntity = carDao.getById(id)
        val gearRatioEntities = gearRatioDao.getForCar(id)
        val powerAtRpmEntities = powerAtRpmDao.getForCar(id)

        return carEntityMapper.toCar(carEntity, gearRatioEntities, powerAtRpmEntities)
    }

    override fun getCars(): List<Car> {
        val carEntities = carDao.getAll()
        val gearRatioEntities = gearRatioDao.getAll().groupBy { it.carId }
        val powerAtRpmEntities = powerAtRpmDao.getAll().groupBy { it.carId }

        return carEntities
            .map {
                carEntityMapper.toCar(
                    it,
                    gearRatioEntities[it.id] ?: listOf(),
                    powerAtRpmEntities[it.id] ?: listOf()
                )
            }
    }

    override fun delete(car: Car) {
        carDao.delete(carEntityMapper.toCarEntity(car))
    }

}