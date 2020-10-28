package hr.alenmagdic.carperformancesimulator.app.dependencyinjection.module

import android.content.Context
import dagger.Module
import dagger.Provides
import hr.alenmagdic.carperformancesimulator.data.dao.CarDao
import hr.alenmagdic.carperformancesimulator.data.dao.GearRatioDao
import hr.alenmagdic.carperformancesimulator.data.dao.PowerAtRpmDao
import hr.alenmagdic.carperformancesimulator.data.CarPerformanceSimulatorDatabase
import hr.alenmagdic.carperformancesimulator.data.DataProvider
import hr.alenmagdic.carperformancesimulator.data.entity.mapper.CarEntityMapper
import hr.alenmagdic.carperformancesimulator.data.entity.mapper.GearRatioEntityMapper
import hr.alenmagdic.carperformancesimulator.data.entity.mapper.PowerAtRpmEntityMapper
import hr.alenmagdic.carperformancesimulator.data.repository.RunningSimulationRepositoryImpl
import hr.alenmagdic.carperformancesimulator.data.repository.CarRepositoryImpl
import hr.alenmagdic.carperformancesimulator.domain.Simulator
import hr.alenmagdic.carperformancesimulator.domain.repository.CarRepository
import hr.alenmagdic.carperformancesimulator.domain.repository.RunningSimulationRepository
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideRunningSimulationRepository(): RunningSimulationRepository {
        return RunningSimulationRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideSimulator(): Simulator {
        return Simulator()
    }

    @Singleton
    @Provides
    fun provideDatabaseProvider(): DataProvider {
        return DataProvider(context)
    }

    @Singleton
    @Provides
    fun provideDatabase(databaseProvider: DataProvider): CarPerformanceSimulatorDatabase {
        return databaseProvider.getDatabase()
    }

    @Singleton
    @Provides
    fun provideCarRepository(
        carDao: CarDao,
        gearRatioDao: GearRatioDao,
        powerAtRpmDao: PowerAtRpmDao,
        carEntityMapper: CarEntityMapper,
        gearRatioEntityMapper: GearRatioEntityMapper,
        powerAtRpmEntityMapper: PowerAtRpmEntityMapper
    ): CarRepository {

        return CarRepositoryImpl(
            carDao,
            gearRatioDao,
            powerAtRpmDao,
            carEntityMapper,
            gearRatioEntityMapper,
            powerAtRpmEntityMapper
        )
    }

    @Singleton
    @Provides
    fun provideCarDao(
        database: CarPerformanceSimulatorDatabase,
        databaseProvider: DataProvider
    ): CarDao {
        return databaseProvider.getCarDao(database)
    }

    @Singleton
    @Provides
    fun provideGearRatioDao(
        database: CarPerformanceSimulatorDatabase,
        databaseProvider: DataProvider
    ): GearRatioDao {
        return databaseProvider.getGearRatioDao(database)
    }

    @Singleton
    @Provides
    fun providePowerAtRpmDao(
        database: CarPerformanceSimulatorDatabase,
        databaseProvider: DataProvider
    ): PowerAtRpmDao {
        return databaseProvider.getPowerAtRpmDao(database)
    }

    @Singleton
    @Provides
    fun provideCarEntityMapper(
        gearRatioEntityMapper: GearRatioEntityMapper,
        powerAtRpmEntityMapper: PowerAtRpmEntityMapper
    ): CarEntityMapper {
        return CarEntityMapper(gearRatioEntityMapper, powerAtRpmEntityMapper)
    }

    @Singleton
    @Provides
    fun provideGearRatioEntityMapper(): GearRatioEntityMapper {
        return GearRatioEntityMapper()
    }

    @Singleton
    @Provides
    fun providePowerAtRpmEntityMapper(): PowerAtRpmEntityMapper {
        return PowerAtRpmEntityMapper()
    }
}