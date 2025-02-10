package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository

class GetCharacterIdxUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(): Int {
        return repository.getCharacterIdx()
    }
}
