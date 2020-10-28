package hr.alenmagdic.carperformancesimulator.domain

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

fun Double.round(decimalPlaces: Int): Double {
    val multiplier = 10.0.pow(decimalPlaces.toDouble())
    return round(this * multiplier) / multiplier
}

fun Double.isEqualTo(other: Double, epsilon: Double = 0.00001): Boolean {
    return abs(other - this) < epsilon
}

fun solveQuadraticEquation(a: Double, b: Double, c: Double): Pair<Double, Double> {
    val sqrtPart = sqrt(b.pow(2.0) - 4 * a * c)
    val denominator = 2 * a
    return Pair(
        (-b + sqrtPart) / denominator,
        (-b - sqrtPart) / denominator
    )
}


