package hr.alenmagdic.carperformancesimulator.data

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.alenmagdic.carperformancesimulator.data.dao.CarDao
import hr.alenmagdic.carperformancesimulator.data.dao.GearRatioDao
import hr.alenmagdic.carperformancesimulator.data.dao.PowerAtRpmDao
import hr.alenmagdic.carperformancesimulator.data.entity.CarEntity
import hr.alenmagdic.carperformancesimulator.data.entity.GearRatioEntity
import hr.alenmagdic.carperformancesimulator.data.entity.PowerAtRpmEntity

@Database(
    entities = [
        CarEntity::class,
        GearRatioEntity::class,
        PowerAtRpmEntity::class
    ],
    version = 1
)
abstract class CarPerformanceSimulatorDatabase : RoomDatabase() {

    abstract fun carDao(): CarDao

    abstract fun gearRatioDao(): GearRatioDao

    abstract fun powerAtRpmDao(): PowerAtRpmDao
}