package com.ggaebiz.ggaebiz.domain.repository

import com.ggaebiz.ggaebiz.domain.model.TopCardInfo

interface StatisticRepository {

    suspend fun getTopCardInfo(): Result<TopCardInfo>

    /** KIKI, BOBO, NANA, CHACHA, BOOBOO 순서의 사용 횟수 리스트. */
    suspend fun getCharacterFrequency(): Result<List<Int>>
}
