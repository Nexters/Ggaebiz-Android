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

/**
 * 캘린더 정보. 요청 월 기준 앞/뒤 포함 3개월.
 * TODO: 응답 envelope(result 래핑)·JSON 구조(playRecord List, dayRecord 배열) 서버 확정 시 조정.
 * TODO: feverDay 포맷("2026-04-15" vs 일 숫자) 미확정 — 파싱 시 마지막 '-' 뒤를 일(day)로 해석.
 */
data class CalendarResponse(
    @SerializedName("result")
    val result: List<CalendarMonthDto> = emptyList(),
)

data class CalendarMonthDto(
    @SerializedName("playRecord")
    val playRecord: PlayRecordDto = PlayRecordDto(),
    @SerializedName("feverDay")
    val feverDay: String? = null,
)

data class PlayRecordDto(
    @SerializedName("yearMonth")
    val yearMonth: String = "",
    @SerializedName("dayRecord")
    val dayRecord: List<Boolean> = emptyList(),
)
