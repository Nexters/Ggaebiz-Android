package com.ggaebiz.ggaebiz.domain.model

/** 상단 카드 원본(서버 값). */
data class TopCardInfo(
    val streakDays: Int,
    val lastAttendanceDate: String?,
)

/** 상단 카드 조립에 필요한 데이터(streak + 캐릭터 빈도) 묶음. */
data class TopCardData(
    val streakDays: Int,
    val lastAttendanceDate: String?,
    val selectionCountList: List<Int>,
)
