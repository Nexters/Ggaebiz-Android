package com.ggaebiz.ggaebiz.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimerDataStore(private val dataStore: DataStore<Preferences>) {

    private val isSettingTimerKey = booleanPreferencesKey("is_setting_timer_data")
    private val characterIdxKey = intPreferencesKey("character_idx_data")
    private val levelKey = intPreferencesKey("level_data")
    private val hourKey = intPreferencesKey("hour_data")
    private val minuteKey = intPreferencesKey("minute_data")
    private val snoozeCountKey = intPreferencesKey("snooze_count_data")

    companion object {
        const val DEFAULT_IS_SETTING_TIMER = false
        const val DEFAULT_CHARACTER_IDX = 0
        const val DEFAULT_LEVEL = 1
        const val DEFAULT_HOUR = 0
        const val DEFAULT_MINUTE = 30
        const val DEFAULT_SNOOZE_COUNT = 0
    }
    
    fun getIsSettingTimer(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isSettingTimerKey] ?: DEFAULT_IS_SETTING_TIMER
        }
    }

    suspend fun setIsSettingTimer(isSettingTimer: Boolean) {
        dataStore.edit { preferences ->
            preferences[isSettingTimerKey] = isSettingTimer
        }
    }

    fun getCharacterIdx(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[characterIdxKey] ?: DEFAULT_CHARACTER_IDX
        }
    }

    suspend fun setCharacterIdx(characterIdx: Int) {
        dataStore.edit { preferences ->
            preferences[characterIdxKey] = characterIdx
        }
    }

    fun getLevel(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[levelKey] ?: DEFAULT_LEVEL
        }
    }

    suspend fun setLevel(level: Int) {
        dataStore.edit { preferences ->
            preferences[levelKey] = level
        }
    }

    fun getHour(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[hourKey] ?: DEFAULT_HOUR
        }
    }

    suspend fun setHour(hour: Int) {
        dataStore.edit { preferences ->
            preferences[hourKey] = hour
        }
    }

    fun getMinute(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[minuteKey] ?: DEFAULT_MINUTE
        }
    }

    suspend fun setMinute(minute: Int) {
        dataStore.edit { preferences ->
            preferences[minuteKey] = minute
        }
    }

    fun getSnoozeCount(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[snoozeCountKey] ?: DEFAULT_SNOOZE_COUNT
        }
    }

    suspend fun setSnoozeCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[snoozeCountKey] = count
        }
    }
}
