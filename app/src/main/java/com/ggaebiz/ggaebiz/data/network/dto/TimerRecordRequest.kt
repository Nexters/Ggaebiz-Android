package com.ggaebiz.ggaebiz.data.network.dto

import com.ggaebiz.ggaebiz.domain.model.TimerRecord
import com.google.gson.annotations.SerializedName

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
