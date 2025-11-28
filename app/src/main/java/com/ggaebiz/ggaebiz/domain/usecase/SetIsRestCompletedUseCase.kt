package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class SetIsRestCompletedUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(isRestCompleted: Boolean) {
        repository.setIsRestCompleted(isRestCompleted)
    }
}
