package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class GetIsRestCompletedUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(): Boolean{
        return repository.getIsRestCompleted()
    }
}
