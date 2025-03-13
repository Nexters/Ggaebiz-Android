package com.ggaebiz.ggaebiz.domain.repository

interface ConfigRepository {

    suspend fun setVibrationStatus(value : Boolean)
    suspend fun getVibrationStatus() : Boolean

    suspend fun setVibrationValue(value : Int)
    suspend fun getVibrationValue() : Int

    suspend fun setVolumeValue(value : Int)
    suspend fun getVolumeValue() : Int

}