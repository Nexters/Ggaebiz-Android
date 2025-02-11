package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class GetTimerSettingUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(): Triple<Int, Int, Int> {
        repository.run {
            return Triple(getLevel(), getHour(), getMinute())
        }
    }
}
