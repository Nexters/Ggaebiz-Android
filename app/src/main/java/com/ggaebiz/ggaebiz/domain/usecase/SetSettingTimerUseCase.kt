package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class SetSettingTimerUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(hour: Int, minute: Int) {
        repository.run {
            setSettingHour(hour)
            setSettingMinute(minute)
        }
    }
}
