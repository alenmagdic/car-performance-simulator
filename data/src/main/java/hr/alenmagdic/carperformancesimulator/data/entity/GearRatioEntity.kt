package hr.alenmagdic.carperformancesimulator.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gear_ratios",
    foreignKeys = [(ForeignKey(
        entity = CarEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("car_id"),
        onDelete = ForeignKey.CASCADE
    ))]
)
data class GearRatioEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "gear_number")
    var gearNumber: Int,

    @ColumnInfo(name = "car_id")
    var carId: Long,

    @ColumnInfo(name = "speed_kmh")
    var speedKmh: Double,

    @ColumnInfo(name = "rpm_at_speed")
    var rpmAtSpeed: Double
)