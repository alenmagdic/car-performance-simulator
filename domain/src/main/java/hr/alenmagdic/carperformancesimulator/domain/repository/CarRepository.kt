package hr.alenmagdic.carperformancesimulator.domain.repository

import hr.alenmagdic.carperformancesimulator.domain.model.Car

interface CarRepository {
    fun save(car: Car)
    fun getCars(): List<Car>
    fun getById(id: Long): Car
    fun delete(car: Car)
}