package com.ggaebiz.ggaebiz.domain.repository

import com.ggaebiz.ggaebiz.data.model.Character

interface AudioRepository {
    suspend fun getAudioResId(character: Character, level: Int, index : Int): String
}
