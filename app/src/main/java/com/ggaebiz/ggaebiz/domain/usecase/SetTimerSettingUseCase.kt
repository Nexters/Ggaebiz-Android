package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class SetTimerSettingUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(level: Int, hour: Int, minute: Int) {
        repository.run {
            setIsSettingTimer(true)
            setLevel(level)
            setHour(hour)
            setMinute(minute)
        }
    }
}
