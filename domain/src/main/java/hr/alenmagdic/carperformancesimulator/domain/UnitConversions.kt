package hr.alenmagdic.carperformancesimulator.domain

object UnitConversions {
    const val KM_PER_HOUR_TO_METERS_PER_SEC = 1 / 3.6
    const val METERS_PER_SEC_TO_KM_PER_HOUR = 3.6
    const val KILOWATT_TO_WATT = 1000
    const val WATT_TO_KILOWATT = 1 / 1000.0
    const val MILIS_TO_SECONDS = 1 / 1000.0
    const val RPM_TO_RPS = 1 / 60.0
    const val RADIANS_TO_DEGRESS = 360 / (2 * Math.PI)
}

fun Double.toMetersPerSec(): Double {
    return this * UnitConversions.KM_PER_HOUR_TO_METERS_PER_SEC
}

fun Double.toKmPerHour(): Double {
    return this * UnitConversions.METERS_PER_SEC_TO_KM_PER_HOUR
}

fun Double.toWatt(): Double {
    return this * UnitConversions.KILOWATT_TO_WATT
}

fun Double.toKilowatt(): Double {
    return this * UnitConversions.WATT_TO_KILOWATT
}

fun Long.toSeconds(): Double {
    return this * UnitConversions.MILIS_TO_SECONDS
}

fun Double.toRotationsPerSec(): Double {
    return this * UnitConversions.RPM_TO_RPS
}

fun Double.toDegrees(): Double {
    return this * UnitConversions.RADIANS_TO_DEGRESS
}