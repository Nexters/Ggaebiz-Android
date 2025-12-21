package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class GetSettingTimerUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(): Pair<Int, Int> {
        repository.run {
            return Pair(getSettingHour(), getSettingMinute())
        }
    }
}
