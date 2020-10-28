package hr.alenmagdic.carperformancesimulator.data.dao

import androidx.room.*
import hr.alenmagdic.carperformancesimulator.data.entity.PowerAtRpmEntity

@Dao
interface PowerAtRpmDao {

    @Query("SELECT * from powers_at_rpm")
    fun getAll(): List<PowerAtRpmEntity>

    @Query("SELECT * from powers_at_rpm WHERE car_id = :carId")
    fun getForCar(carId: Long): List<PowerAtRpmEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(powerAtRpm: PowerAtRpmEntity)

    @Delete
    fun delete(powerAtRpm: PowerAtRpmEntity)

    @Query("DELETE from powers_at_rpm WHERE car_id = :carId")
    fun deleteForCar(carId: Long)
}