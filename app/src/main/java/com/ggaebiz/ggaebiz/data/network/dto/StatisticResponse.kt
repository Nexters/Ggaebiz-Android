package com.ggaebiz.ggaebiz.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * 상단 카드 데이터.
 * TODO: 응답 envelope(result 래핑) 미확정. 서버 확정 시 조정.
 * userId 는 body 에 없음 — Authorization 헤더(Bearer)로 서버가 식별.
 */
data class TopCardResponse(
    @SerializedName("streakDays")
    val streakDays: Int = 0,
    @SerializedName("lastAttendanceDate")
    val lastAttendanceDate: String? = null,
)

/**
 * 캐릭터 사용 빈도. KIKI, BOBO, NANA, CHACHA, BOOBOO 순서.
 */
data class CharacterFrequencyResponse(
    @SerializedName("selectionCountList")
    val selectionCountList: List<Int> = emptyList(),
)

/**
 * TODO: 응답 envelope(result 래핑) 미확정. 서버 확정 시 조정.
 * TODO: mode 값 스펙 표기는 "CONCENRATE"(오타) — 클라는 "CONCENTRATE"/"CONCENRATE" 모두 허용.
 */
data class TimerTimesResponse(
    @SerializedName("result")
    val result: List<TimerTimeDto> = emptyList(),
)

data class TimerTimeDto(
    @SerializedName("mode")
    val mode: String = "",
    @SerializedName("concentrateType")
    val concentrateType: String? = null,
    @SerializedName("timeType")
    val timeType: String = "",
    @SerializedName("time")
    val time: Long = 0L,
)
