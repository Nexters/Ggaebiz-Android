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


    companion object {
        const val DEFAULT_IS_VIBRATION = true
        const val DEFAULT_VIBRATION = 3
        const val DEFAULT_VOLUME = 3
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
}
