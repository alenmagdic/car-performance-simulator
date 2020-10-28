package hr.alenmagdic.carperformancesimulator.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "powers_at_rpm",
    foreignKeys = [(ForeignKey(
        entity = CarEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("car_id"),
        onDelete = ForeignKey.CASCADE
    ))]
)
data class PowerAtRpmEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "car_id")
    var carId: Long,

    @ColumnInfo(name = "power")
    var power: Double,

    @ColumnInfo(name = "rpm")
    var rpm: Double
)