package com.ggaebiz.ggaebiz.data.network

import com.ggaebiz.ggaebiz.data.network.dto.TimerRecordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TimerApi {
    // TODO: 엔드포인트 미확정(임시). 서버 확정 시 경로 교체.
    @POST("api/timer/records")
    suspend fun sendTimerRecords(
        @Body records: List<TimerRecordRequest>,
    ): Response<Unit>
}
