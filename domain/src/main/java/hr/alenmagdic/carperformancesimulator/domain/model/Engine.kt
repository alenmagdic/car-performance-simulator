package hr.alenmagdic.carperformancesimulator.domain.model

data class Engine(
    var maxRpm: Double,
    var idleRpm: Double,
    var compressionEnergy: Double,
    var power: List<PowerAtRpm>
) {

    fun getPowerAtRpm(rpm: Double): Double {
        if (power.first().rpm >= rpm) return 0.0

        power.forEachIndexed { index, powerAtRpm ->
            if (powerAtRpm.rpm >= rpm) {
                val previousPowerAtRpm = power[index - 1]
                return interpolatePowerAtRpm(previousPowerAtRpm, powerAtRpm, rpm)
            }
        }

        return 0.0
    }

    fun getTorqueAtRpm(rpm: Double): Double {
        return getPowerAtRpm(rpm) * 1000 / (2 * Math.PI * rpm / 60.0)
    }

    private fun interpolatePowerAtRpm(
        powerAtLowerRpm: PowerAtRpm,
        powerAtHigherRpm: PowerAtRpm,
        rpm: Double
    ): Double {
        val lowerRpmPower = powerAtLowerRpm.power
        val higherRpmPower = powerAtHigherRpm.power
        val lowerRpm = powerAtLowerRpm.rpm
        val higherRpm = powerAtHigherRpm.rpm

        return lowerRpmPower + (rpm - lowerRpm) / (higherRpm - lowerRpm) * (higherRpmPower - lowerRpmPower)
    }

    fun isValid(): Boolean {
        return maxRpm > 0 &&
                idleRpm > 0 &&
                compressionEnergy >= 0 &&
                power.isNotEmpty() &&
                power.all { it.isValid() }
    }
}