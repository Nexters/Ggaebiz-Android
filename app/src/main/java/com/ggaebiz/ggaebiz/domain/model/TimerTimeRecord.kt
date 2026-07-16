package com.ggaebiz.ggaebiz.domain.model

/**
 * 타이머 기록 가져오기(집중/휴식 시간 카드용) 한 행.
 * 서버는 mode × concentrateType × timeType 조합으로 12행을 내려준다.
 */
data class TimerTimeRecord(
    val mode: String,
    val concentrateType: String?,
    val timeType: String,
    val time: Long,
)
