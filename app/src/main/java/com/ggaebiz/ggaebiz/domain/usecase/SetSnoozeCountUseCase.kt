package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class SetSnoozeCountUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(count : Int){
        return repository.setSnoozeCount(count)
    }
}