package com.ggaebiz.ggaebiz.data.repository

import android.util.Log
import com.ggaebiz.ggaebiz.data.datastore.TimerDataStore
import com.ggaebiz.ggaebiz.data.datastore.TimerDataStore.Companion.DEFAULT_CHARACTER_IDX
import com.ggaebiz.ggaebiz.data.datastore.TimerDataStore.Companion.DEFAULT_HOUR
import com.ggaebiz.ggaebiz.data.datastore.TimerDataStore.Companion.DEFAULT_IS_SETTING_TIMER
import com.ggaebiz.ggaebiz.data.datastore.TimerDataStore.Companion.DEFAULT_LEVEL
import com.ggaebiz.ggaebiz.data.datastore.TimerDataStore.Companion.DEFAULT_MINUTE
import com.ggaebiz.ggaebiz.domain.repository.TimerRepository
import kotlinx.coroutines.flow.first

class TimerRepositoryImpl(
    private val timerDataStore: TimerDataStore,
) : TimerRepository {

    override suspend fun getIsSettingTimer(): Boolean {
        return try {
            timerDataStore.getIsSettingTimer().first()
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error fetching timer data", e)
            DEFAULT_IS_SETTING_TIMER
        }
    }

    override suspend fun setIsSettingTimer(isSettingTimer: Boolean) {
        try {
            timerDataStore.setIsSettingTimer(isSettingTimer)
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error updating timer data", e)
        }
    }

    override suspend fun getCharacterIdx(): Int {
        return try {
            timerDataStore.getCharacterIdx().first()
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error fetching timer data", e)
            DEFAULT_CHARACTER_IDX
        }
    }

    override suspend fun setCharacterIdx(characterIdx: Int) {
        try {
            timerDataStore.setCharacterIdx(characterIdx)
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error updating timer data", e)
        }
    }

    override suspend fun getLevel(): Int {
        return try {
            timerDataStore.getLevel().first()
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error fetching timer data", e)
            DEFAULT_LEVEL
        }
    }

    override suspend fun setLevel(level: Int) {
        try {
            timerDataStore.setLevel(level)
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error updating timer data", e)
        }
    }

    override suspend fun getHour(): Int {
        return try {
            timerDataStore.getHour().first()
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error fetching timer data", e)
            DEFAULT_HOUR
        }
    }

    override suspend fun setHour(hour: Int) {
        try {
            timerDataStore.setHour(hour)
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error updating timer data", e)
        }
    }

    override suspend fun getMinute(): Int {
        return try {
            timerDataStore.getMinute().first()
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error fetching timer data", e)
            DEFAULT_MINUTE
        }
    }

    override suspend fun setMinute(minute: Int) {
        try {
            timerDataStore.setMinute(minute)
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error updating timer data", e)
        }
    }

    override suspend fun getSnoozeCount(): Int {
        return try {
            timerDataStore.getSnoozeCount().first()
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error updating timer data", e)
        }
    }

    override suspend fun setSnoozeCount(count: Int) {
        try {
            timerDataStore.setSnoozeCount(count)
        } catch (e: Exception) {
            Log.e("TimerRepositoryImpl", "Error updating timer data", e)
        }
    }
}



