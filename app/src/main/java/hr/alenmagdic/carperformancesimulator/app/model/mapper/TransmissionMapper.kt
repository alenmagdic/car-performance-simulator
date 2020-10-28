package hr.alenmagdic.carperformancesimulator.app.model.mapper

import hr.alenmagdic.carperformancesimulator.app.model.TransmissionModel
import hr.alenmagdic.carperformancesimulator.domain.model.Transmission
import javax.inject.Inject

class TransmissionMapper @Inject constructor(private val gearRatioMapper: GearRatioMapper) {

    fun toTransmission(transmissionModel: TransmissionModel): Transmission {
        return Transmission(
            transmissionModel.gearChangeLengthMilis!!,
            transmissionModel.gearRatios.map { gearRatioMapper.toGearRatio(it) }
        )
    }

    fun toTransmissionModel(transmission: Transmission): TransmissionModel {
        return TransmissionModel(
            transmission.gearChangeLengthMilis,
            transmission.gearRatios.map { gearRatioMapper.toGearRatioModel(it) }
        )
    }
}