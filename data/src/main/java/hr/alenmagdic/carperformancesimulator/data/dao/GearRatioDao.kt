package hr.alenmagdic.carperformancesimulator.data.dao

import androidx.room.*
import hr.alenmagdic.carperformancesimulator.data.entity.GearRatioEntity

@Dao
interface GearRatioDao {

    @Query("SELECT * from gear_ratios")
    fun getAll(): List<GearRatioEntity>

    @Query("SELECT * from gear_ratios WHERE car_id = :carId ORDER BY gear_number ASC")
    fun getForCar(carId: Long): List<GearRatioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(gearRatio: GearRatioEntity)

    @Delete
    fun delete(gearRatio: GearRatioEntity)

    @Query("DELETE from gear_ratios WHERE car_id = :carId")
    fun deleteForCar(carId: Long)
}