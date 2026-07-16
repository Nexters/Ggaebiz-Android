package com.ggaebiz.ggaebiz.data.network

import com.ggaebiz.ggaebiz.data.network.dto.CharacterFrequencyResponse
import com.ggaebiz.ggaebiz.data.network.dto.TimerTimesResponse
import com.ggaebiz.ggaebiz.data.network.dto.TopCardResponse
import retrofit2.http.GET

// TODO: 엔드포인트 미확정(임시). 서버 확정 시 경로 교체.
interface StatisticApi {
    @GET("api/statistic/top-card")
    suspend fun getTopCard(): TopCardResponse

    @GET("api/statistic/character-frequency")
    suspend fun getCharacterFrequency(): CharacterFrequencyResponse

    @GET("api/statistic/timer-times")
    suspend fun getTimerTimes(): TimerTimesResponse
}
