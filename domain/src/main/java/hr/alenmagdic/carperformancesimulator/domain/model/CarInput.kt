package hr.alenmagdic.carperformancesimulator.domain.model

import hr.alenmagdic.carperformancesimulator.domain.isEqualTo

data class CarInput(
    var throttleInput: Double = 0.0,
    var brakeInput: Double = 0.0,
    var clutchInput: Double = 0.0
) {
    companion object {
        const val FULL_INPUT = 1.0
        const val NO_INPUT = 0.0
    }

    fun isNoThrottleInput(): Boolean {
        return throttleInput.isEqualTo(NO_INPUT)
    }

    fun isFullClutchInput(): Boolean {
        return clutchInput.isEqualTo(FULL_INPUT)
    }
}