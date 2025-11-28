package com.ggaebiz.ggaebiz.domain.repository

import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode

interface TimerRepository {

    suspend fun getIsSettingTimer(): Boolean
    suspend fun setIsSettingTimer(isSettingTimer: Boolean)

    suspend fun getCharacterIdx(): Int
    suspend fun setCharacterIdx(characterIdx: Int)

    suspend fun getLevel(): Int
    suspend fun setLevel(level: Int)

    suspend fun getHour(): Int
    suspend fun setHour(hour: Int)

    suspend fun getMinute(): Int
    suspend fun setMinute(minute: Int)

    suspend fun getSettingHour(): Int
    suspend fun setSettingHour(settingHour: Int)

    suspend fun getSettingMinute(): Int
    suspend fun setSettingMinute(settingMinute: Int)

    suspend fun getIsRestCompleted(): Boolean
    suspend fun setIsRestCompleted(isRestCompleted: Boolean)

    suspend fun getTimerMode(): TimerMode
    suspend fun setTimerMode(timerMode: TimerMode)

    suspend fun getSnoozeCount(): Int
    suspend fun setSnoozeCount(count: Int)

    suspend fun getLevelIdx(): Int
}
