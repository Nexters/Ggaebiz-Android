package com.ggaebiz.ggaebiz.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OnboardingDataStore(private val dataStore: DataStore<Preferences>) {

    private val isOnboardingExposed = booleanPreferencesKey("is_onboarding_exposed")
    private val isHomeNudgeGuideViewed = booleanPreferencesKey("is_home_nudge_viewed")
    private val isSettingNudgeGuideViewed = booleanPreferencesKey("is_setting_nudge_viewed")
    private val isBatteryPopupViewed = booleanPreferencesKey("is_battery_popup_viewed")

    companion object {
        const val DEFAULT_IS_ONBOARDING_EXPOSED = false
        const val DEFAULT_IS_HOME_NUDGE_GUIDE_VIEWED = false
        const val DEFAULT_IS_SETTING_NUDGE_GUIDE_VIEWED = false
        const val DEFAULT_IS_BATTERY_POPUP_VIEWED = false
    }

    suspend fun clearOnboardingInfo() {
        dataStore.edit { preferences ->
            preferences.remove(isOnboardingExposed)
            preferences.remove(isHomeNudgeGuideViewed)
            preferences.remove(isSettingNudgeGuideViewed)
            preferences.remove(isBatteryPopupViewed)
        }
    }

    suspend fun setIsOnboardingExposed(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[isOnboardingExposed] = value
        }
    }

    fun getIsOnboardingExposed(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isOnboardingExposed] ?: DEFAULT_IS_ONBOARDING_EXPOSED
        }
    }

    suspend fun setHomeNudgeGuideViewed(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[isHomeNudgeGuideViewed] = value
        }
    }

    fun getHomeNudgeGuideViewed(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isHomeNudgeGuideViewed] ?: DEFAULT_IS_HOME_NUDGE_GUIDE_VIEWED
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
