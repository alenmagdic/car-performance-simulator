package hr.alenmagdic.carperformancesimulator.data.dao

import androidx.room.*
import hr.alenmagdic.carperformancesimulator.data.entity.CarEntity

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun getAll(): List<CarEntity>

    @Query("SELECT * FROM cars WHERE id=:carId")
    fun getById(carId: Long): CarEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(car: CarEntity): Long

    @Delete
    fun delete(car: CarEntity)
}