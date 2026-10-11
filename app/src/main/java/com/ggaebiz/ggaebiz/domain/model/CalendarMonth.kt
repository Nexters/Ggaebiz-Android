package com.ggaebiz.ggaebiz.domain.model

/**
 * 캘린더 한 달 데이터.
 * @param yearMonth "yyyy-MM"
 * @param dayRecord 그 달 각 날의 집중 타이머 기록 여부(index 0 = 1일)
 * @param feverDay 그 달 가장 집중한 일(day). 없으면 null.
 */
data class CalendarMonth(
    val yearMonth: String,
    val dayRecord: List<Boolean>,
    val feverDay: Int?,
)
