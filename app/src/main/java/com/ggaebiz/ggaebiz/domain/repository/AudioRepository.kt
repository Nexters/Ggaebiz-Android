package com.ggaebiz.ggaebiz.domain.repository

import com.ggaebiz.ggaebiz.data.model.CharacterName

interface AudioRepository {
    suspend fun getAudioResId(characterName: CharacterName, level: Int, index : Int): String
}
