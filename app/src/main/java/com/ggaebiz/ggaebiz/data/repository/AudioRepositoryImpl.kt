package com.ggaebiz.ggaebiz.data.repository

import android.content.Context
import android.util.Log
import com.ggaebiz.ggaebiz.data.datastore.AudioDataStore
import com.ggaebiz.ggaebiz.data.model.Character
import com.ggaebiz.ggaebiz.domain.repository.AudioRepository
import kotlinx.coroutines.flow.first

class AudioRepositoryImpl(
    private val context: Context,
    private val audioDataStore: AudioDataStore
) : AudioRepository {

    override suspend fun getAudioResId(character: Character, level: Int, index: Int): Int {
        return try {
            val audioMap = audioDataStore.getAudioMap().first()
            val resourceName = audioMap[character]?.get(3)?.get((0))
                ?: throw IllegalArgumentException("음원 정보 없음")

            getRawResId(resourceName)
        } catch (e: Exception) {
            Log.e("AudioRepositoryImpl", "Error fetching audio data", e)
        }
    }

    // 음원 파일의 Resource ID 찾기
    private fun getRawResId(resourceName: String): Int {
        val resName = resourceName.replace("raw/", "")
        return context.resources.getIdentifier(resName, "raw", context.packageName)
    }
}
