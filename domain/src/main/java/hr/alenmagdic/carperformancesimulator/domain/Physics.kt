package hr.alenmagdic.carperformancesimulator.domain

import kotlin.math.pow
import kotlin.math.sqrt

object Physics {

    fun calculateKineticEnergy(weight: Double, speedMPerS: Double) =
        weight * speedMPerS.pow(2.0) / 2

    fun calculateSpeedMPerSFromKineticEnergy(kineticEnergy: Double, weight: Double) =
        sqrt(2.0 / weight * kineticEnergy)

    fun calculateAirResistance(
        speedMPerS: Double,
        airDensity: Double,
        dragCoefficient: Double,
        frontArea: Double
    ) =
        0.5 * airDensity * speedMPerS.pow(2.0) * dragCoefficient * frontArea

    fun calculateRollResistance(
        tirePressure: Double,
        speedKmH: Double,
        weight: Double,
        gravityAcc: Double
    ): Double {
        val rollResistCoeff =
            (0.005 + (1.0 / tirePressure) * (0.01 + 0.0095 * (speedKmH / 100.0).pow(2.0)))
        return rollResistCoeff * weight * gravityAcc
    }

}