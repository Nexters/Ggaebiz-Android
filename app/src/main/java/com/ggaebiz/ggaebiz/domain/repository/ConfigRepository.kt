package com.ggaebiz.ggaebiz.domain.repository

interface ConfigRepository {

    suspend fun setVibrationStatus(value: Boolean)
    suspend fun getVibrationStatus(): Boolean

    suspend fun setVibrationValue(value: Int)
    suspend fun getVibrationValue(): Int

    suspend fun setVolumeValue(value: Int)
    suspend fun getVolumeValue(): Int

    suspend fun setHomeNudgeGuideViewed(value: Boolean)
    suspend fun getHomeNudgeGuideViewed(): Boolean

    suspend fun setSettingNudgeGuideViewed(value: Boolean)
    suspend fun getSettingNudgeGuideViewed(): Boolean

    suspend fun setBatteryPopupViewed(value: Boolean)
    suspend fun getBatteryPopupViewed(): Boolean

}