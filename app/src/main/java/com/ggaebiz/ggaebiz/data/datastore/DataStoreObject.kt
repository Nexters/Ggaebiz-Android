package com.ggaebiz.ggaebiz.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.ggaebiz.ggaebiz.data.datastore.DataStoreParser.parseJsonToMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

object DataStoreObject {
    private const val AUDIOS_DATASTORE_NAME = "AUDIOS_PREFERENCES"
    val Context.audioDataStore by preferencesDataStore(AUDIOS_DATASTORE_NAME)

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