package com.ggaebiz.ggaebiz.data.network.dto

import com.ggaebiz.ggaebiz.domain.model.TimerRecord
import com.google.gson.annotations.SerializedName

/**
 * TODO: 스펙 표기는 mode 값이 "CONCENRATE"(오타) 이나 여기선 코드 컨벤션대로 "CONCENTRATE" 를 보낸다.
 *       서버와 문자열 계약 확정 필요.
 */
data class TimerRecordRequest(
    @SerializedName("gaebiz")
    val gaebiz: String,
    @SerializedName("time")
    val time: Long,
    @SerializedName("mode")
    val mode: String,
    @SerializedName("concentrateType")
    val concentrateType: String?,
    @SerializedName("playAt")
    val playAt: String,
    @SerializedName("restLevel")
    val restLevel: Int,
)

fun TimerRecord.toRequest(): TimerRecordRequest = TimerRecordRequest(
    gaebiz = gaebiz,
    time = time,
    mode = mode,
    concentrateType = concentrateType,
    playAt = playAt,
    restLevel = restLevel,
)
