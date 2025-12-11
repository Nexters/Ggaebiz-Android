package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode

class SetCurrentTimerUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(
        level: Int,
        hour: Int,
        minute: Int,
        timerMode: TimerMode?,
        isIntervalTimer: Boolean
    ) {
        repository.run {
            setIsSettingTimer(true)
            setLevel(level)
            setHour(hour)
            setMinute(minute)
            timerMode?.let { setTimerMode(it) }
            setIsIntervalTimer(isIntervalTimer)
        }
    }
}
