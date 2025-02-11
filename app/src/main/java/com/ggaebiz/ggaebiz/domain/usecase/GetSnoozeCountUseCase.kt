package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class GetSnoozeCountUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(): Int{
        return repository.getSnoozeCount()
    }
}