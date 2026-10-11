package com.ggaebiz.ggaebiz.domain.model

data class TimerRecord(
    val gaebiz: String,
    val time: Long,
    val mode: String,
    val concentrateType: String?,
    val playAt: String,
    val restLevel: Int,           // 휴식 레벨 (TODO: 서버의 restLevel 정의 확정 후 매핑 조정)
)
