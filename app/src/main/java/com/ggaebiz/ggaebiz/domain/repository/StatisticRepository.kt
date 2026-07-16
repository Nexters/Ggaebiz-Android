package com.ggaebiz.ggaebiz.domain.repository

import com.ggaebiz.ggaebiz.domain.model.TimerTimeRecord
import com.ggaebiz.ggaebiz.domain.model.TopCardInfo

interface StatisticRepository {

    suspend fun getTopCardInfo(): Result<TopCardInfo>

    /** KIKI, BOBO, NANA, CHACHA, BOOBOO 순서의 사용 횟수 리스트. */
    suspend fun getCharacterFrequency(): Result<List<Int>>

    /** 집중/휴식 시간 카드용 12행(mode × concentrateType × timeType). */
    suspend fun getTimerTimes(): Result<List<TimerTimeRecord>>
}
