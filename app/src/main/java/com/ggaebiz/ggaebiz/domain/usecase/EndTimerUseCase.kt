package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class EndTimerUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke() {
        repository.setIsSettingTimer(false)
    }
}
