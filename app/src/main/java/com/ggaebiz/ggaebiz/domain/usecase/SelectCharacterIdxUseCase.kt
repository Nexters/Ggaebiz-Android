package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class SelectCharacterIdxUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(index : Int) {
        repository.setCharacterIdx(index)
    }
}
