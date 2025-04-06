package com.ggaebiz.ggaebiz.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConfigDataStore(private val dataStore: DataStore<Preferences>) {

    private val isVibrationStatus = booleanPreferencesKey("is_vibration_status")
    private val vibrationValue = intPreferencesKey("vibration_value")
    private val volumeValue = intPreferencesKey("volume_value")
    private val isHomeNudgeGuideViewed = booleanPreferencesKey("home_nudge_viewed")
    private val isSettingNudgeGuideViewed = booleanPreferencesKey("setting_nudge_viewed")
    private val isBatteryPopupViewed = booleanPreferencesKey("battery_popup_viewed")

    companion object {
        const val DEFAULT_IS_VIBRATION = true
        const val DEFAULT_VIBRATION = 3
        const val DEFAULT_VOLUME = 3
        const val DEFAULT_IS_HOME_NUDGE_GUIDE_VIEWED = false
        const val DEFAULT_IS_SETTING_NUDGE_GUIDE_VIEWED = false
        const val DEFAULT_IS_BATTERY_POPUP_VIEWED = false
    }

    suspend fun setVibrationStatus(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[isVibrationStatus] = value
        }
    }

    fun getVibrationStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isVibrationStatus] ?: DEFAULT_IS_VIBRATION
        }
    }

    suspend fun setVibrationValue(value: Int) {
        dataStore.edit { preferences ->
            preferences[vibrationValue] = value
        }
    }

    fun getVibrationValue(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[vibrationValue] ?: DEFAULT_VIBRATION
        }
    }

    suspend fun setVolumeValue(value: Int) {
        dataStore.edit { preferences ->
            preferences[volumeValue] = value
        }
    }

    fun getVolumeValue(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[volumeValue] ?: DEFAULT_VOLUME
        }
    }

    suspend fun setHomeNudgeGuideViewed(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[isHomeNudgeGuideViewed] = value
        }
    }

    fun getHomeNudgeGuideViewed(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isHomeNudgeGuideViewed] ?: false
        }
    }

    suspend fun setSettingNudgeGuideViewed(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[isSettingNudgeGuideViewed] = value
        }
    }

    fun getSettingNudgeGuideViewed(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isSettingNudgeGuideViewed] ?: DEFAULT_IS_SETTING_NUDGE_GUIDE_VIEWED
        }
    }
    suspend fun setBatteryPopupViewed(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[isBatteryPopupViewed] = value
        }
    }

    fun getBatteryPopupViewed(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isBatteryPopupViewed] ?: DEFAULT_IS_BATTERY_POPUP_VIEWED
        }
    }

}
