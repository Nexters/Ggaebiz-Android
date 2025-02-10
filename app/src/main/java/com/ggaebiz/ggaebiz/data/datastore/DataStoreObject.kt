package com.ggaebiz.ggaebiz.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

object DataStoreObject {
    private const val AUDIOS_DATASTORE_NAME = "audios_preferences"
    val Context.audioDataStore by preferencesDataStore(AUDIOS_DATASTORE_NAME)

    private const val TIMER_DATASTORE_NAME = "timer_preferences"
    val Context.timerDataStore by preferencesDataStore(TIMER_DATASTORE_NAME)

    // 최초 1회 호출.
    suspend fun initialize(context: Context, audioDataStore: AudioDataStore) {
        val existingData = audioDataStore.getAudioMap().firstOrNull()
        if (!existingData.isNullOrEmpty()) { return }

        val json = withContext(Dispatchers.IO) {
            context.assets.open("audio.json").bufferedReader().use { it.readText() }
        }
        audioDataStore.saveAudioData(json)
    }
}
