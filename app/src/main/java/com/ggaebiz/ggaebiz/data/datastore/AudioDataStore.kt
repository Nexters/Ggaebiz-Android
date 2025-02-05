package com.ggaebiz.ggaebiz.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ggaebiz.ggaebiz.data.datastore.DataStoreParser.parseJsonToMap
import com.ggaebiz.ggaebiz.data.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AudioDataStore(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val AUDIO_KEY = stringPreferencesKey("audio_data")
    }

    fun getAudioMap(): Flow<Map<Character, Map<Int, List<String>>>> {
        return dataStore.data.map { preferences ->
            preferences[AUDIO_KEY]?.let { json ->
                parseJsonToMap(json)
            } ?: emptyMap()
        }
    }


    suspend fun saveAudioData(json: String) {
        dataStore.edit { preferences ->
            preferences[AUDIO_KEY] = json
        }
    }
}
