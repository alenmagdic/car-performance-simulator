package hr.alenmagdic.carperformancesimulator.domain.usecase.cars

import hr.alenmagdic.carperformancesimulator.domain.model.*
import hr.alenmagdic.carperformancesimulator.domain.repository.CarRepository
import io.reactivex.Single

class GetCarsUseCase(private val carRepository: CarRepository) {

    fun execute(): Single<List<Car>> {
        return Single.fromCallable {
            carRepository.getCars()
        }
    }
}