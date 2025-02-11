package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.ggaebiz.ggaebiz.domain.repository.AudioRepository

class GetAudioResIdUseCase(private val repository: AudioRepository) {
    suspend operator fun invoke(characterName: CharacterName, level: Int, index : Int): String {
        return repository.getAudioResId(characterName, level, index)
    }
}
