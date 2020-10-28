package hr.alenmagdic.carperformancesimulator.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "weight")
    var weight: Double,

    @ColumnInfo(name = "drag_coefficient")
    var dragCoefficient: Double,

    @ColumnInfo(name = "front_area")
    var frontArea: Double,

    @ColumnInfo(name = "tire_pressure")
    var tirePressure: Double,

    @ColumnInfo(name = "brake_force")
    var brakeForce: Int,

    @ColumnInfo(name = "gear_change_duration")
    var gearChangeLengthMilis: Long,

    @ColumnInfo(name = "max_rpm")
    var maxRpm: Double,

    @ColumnInfo(name = "idle_rpm")
    var idleRpm: Double,

    @ColumnInfo(name = "compression_energy")
    var compressionEnergy: Double,

    @ColumnInfo(name = "max_speedometer_speed")
    var maxSpeedometerSpeed: Int,

    @ColumnInfo(name = "speedometer_segments_count")
    var speedometerSegmentsCount: Int,

    @ColumnInfo(name = "speedometer_labeled_marks_step")
    var speedometerLabeledMarksStep: Int,

    @ColumnInfo(name = "max_rpm_meter_value")
    var maxRpmMeterValue: Int,

    @ColumnInfo(name = "rpm_meter_segments_count")
    var rpmMeterSegmentsCount: Int,

    @ColumnInfo(name = "rpm_meter_labeled_marks_step")
    var rpmMeterLabeledMarksStep: Int,

    @ColumnInfo(name = "rpm_meter_critical_rpm_threshold")
    var rpmMeterCriticalRpmThreshold: Int,

    @ColumnInfo(name = "rpm_meter_value_label_multiplicator")
    var rpmMeterValueLabelMultiplicator: Int
)