package com.ggaebiz.ggaebiz.data.repository

import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore.Companion.DEFAULT_IS_BATTERY_POPUP_VIEWED
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore.Companion.DEFAULT_IS_HOME_NUDGE_GUIDE_VIEWED
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore.Companion.DEFAULT_IS_SETTING_NUDGE_GUIDE_VIEWED
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore.Companion.DEFAULT_IS_VIBRATION
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore.Companion.DEFAULT_VIBRATION
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore.Companion.DEFAULT_VOLUME
import com.ggaebiz.ggaebiz.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.firstOrNull

class ConfigRepositoryImpl(
    private val configDataStore: ConfigDataStore,
) : ConfigRepository {
    override suspend fun setVibrationStatus(value: Boolean) =
        configDataStore.setVibrationStatus(value)

    override suspend fun getVibrationStatus(): Boolean =
        configDataStore.getVibrationStatus().firstOrNull() ?: DEFAULT_IS_VIBRATION

    override suspend fun setVibrationValue(value: Int) = configDataStore.setVibrationValue(value)
    override suspend fun getVibrationValue(): Int =
        configDataStore.getVibrationValue().firstOrNull() ?: DEFAULT_VIBRATION

    override suspend fun setVolumeValue(value: Int) = configDataStore.setVolumeValue(value)

    override suspend fun getVolumeValue(): Int =
        configDataStore.getVolumeValue().firstOrNull() ?: DEFAULT_VOLUME

    override suspend fun setHomeNudgeGuideViewed(value: Boolean) =
        configDataStore.setHomeNudgeGuideViewed(value)

    override suspend fun getHomeNudgeGuideViewed(): Boolean =
        configDataStore.getHomeNudgeGuideViewed().firstOrNull() ?: DEFAULT_IS_HOME_NUDGE_GUIDE_VIEWED

    override suspend fun setSettingNudgeGuideViewed(value: Boolean) =
        configDataStore.setSettingNudgeGuideViewed(value)

    override suspend fun getSettingNudgeGuideViewed(): Boolean =
        configDataStore.getSettingNudgeGuideViewed().firstOrNull() ?: DEFAULT_IS_SETTING_NUDGE_GUIDE_VIEWED

    override suspend fun setBatteryPopupViewed(value: Boolean) =
        configDataStore.setBatteryPopupViewed(value)

    override suspend fun getBatteryPopupViewed(): Boolean =
        configDataStore.getBatteryPopupViewed().firstOrNull() ?: DEFAULT_IS_BATTERY_POPUP_VIEWED

}