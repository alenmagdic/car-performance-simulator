package hr.alenmagdic.carperformancesimulator.domain.usecase.cars

import hr.alenmagdic.carperformancesimulator.domain.model.Car
import hr.alenmagdic.carperformancesimulator.domain.repository.CarRepository
import io.reactivex.Completable

class SaveCarUseCase(private val carRepository: CarRepository) {

    fun execute(car: Car): Completable {
        return Completable.fromAction {
            carRepository.save(car)
        }
    }
}