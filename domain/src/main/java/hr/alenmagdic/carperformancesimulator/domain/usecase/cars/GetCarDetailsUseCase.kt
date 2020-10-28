package hr.alenmagdic.carperformancesimulator.domain.usecase.cars

import hr.alenmagdic.carperformancesimulator.domain.model.*
import hr.alenmagdic.carperformancesimulator.domain.repository.CarRepository
import io.reactivex.Single

class GetCarDetailsUseCase(private val carRepository: CarRepository) {

    fun execute(carId: Long): Single<Car> =
        Single.fromCallable {
            carRepository.getById(carId)
        }

}