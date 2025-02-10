package com.ggaebiz.ggaebiz.domain.repository

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
}
