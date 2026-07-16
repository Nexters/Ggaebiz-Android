package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.model.TimerTimeRecord
import com.ggaebiz.ggaebiz.domain.repository.StatisticRepository

class GetTimerTimesUseCase(private val repository: StatisticRepository) {
    suspend operator fun invoke(): Result<List<TimerTimeRecord>> = repository.getTimerTimes()
}
