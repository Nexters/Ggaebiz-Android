package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.data.model.Character
import com.ggaebiz.ggaebiz.domain.repository.AudioRepository

class GetAudioResIdUseCase(private val repository: AudioRepository) {
    suspend operator fun invoke(character: Character, level: Int, index : Int): Int {
        return repository.getAudioResId(character, level, index)
    }
}
