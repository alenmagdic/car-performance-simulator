package hr.alenmagdic.carperformancesimulator.data

import android.content.Context
import androidx.room.Room
import hr.alenmagdic.carperformancesimulator.data.dao.CarDao
import hr.alenmagdic.carperformancesimulator.data.dao.GearRatioDao
import hr.alenmagdic.carperformancesimulator.data.dao.PowerAtRpmDao

class DataProvider(private val context: Context) {

    fun getDatabase(): CarPerformanceSimulatorDatabase {
        return Room.databaseBuilder(
            context,
            CarPerformanceSimulatorDatabase::class.java,
            DATABASE_NAME
        )
            .build()
    }

    fun getCarDao(database: CarPerformanceSimulatorDatabase): CarDao {
        return database.carDao()
    }

    fun getGearRatioDao(database: CarPerformanceSimulatorDatabase): GearRatioDao {
        return database.gearRatioDao()
    }

    fun getPowerAtRpmDao(database: CarPerformanceSimulatorDatabase): PowerAtRpmDao {
        return database.powerAtRpmDao()
    }

    companion object {
        const val DATABASE_NAME = "car_performance_simulator_db"
    }
}