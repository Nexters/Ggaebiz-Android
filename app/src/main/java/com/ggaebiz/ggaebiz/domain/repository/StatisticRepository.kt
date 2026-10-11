package com.ggaebiz.ggaebiz.domain.repository

import com.ggaebiz.ggaebiz.domain.model.CalendarMonth
import com.ggaebiz.ggaebiz.domain.model.TimerTimeRecord
import com.ggaebiz.ggaebiz.domain.model.TopCardInfo

interface StatisticRepository {

    suspend fun getTopCardInfo(): Result<TopCardInfo>

    /** KIKI, BOBO, NANA, CHACHA, BOOBOO 순서의 사용 횟수 리스트. */
    suspend fun getCharacterFrequency(): Result<List<Int>>

    /** 집중/휴식 시간 카드용 12행(mode × concentrateType × timeType). */
    suspend fun getTimerTimes(): Result<List<TimerTimeRecord>>

    /** 캘린더 — yearMonth("yyyy-MM") 기준 앞/뒤 포함 3개월. */
    suspend fun getCalendar(yearMonth: String): Result<List<CalendarMonth>>
}
