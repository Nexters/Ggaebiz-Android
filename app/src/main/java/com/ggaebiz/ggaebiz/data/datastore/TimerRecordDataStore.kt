package com.ggaebiz.ggaebiz.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ggaebiz.ggaebiz.domain.model.TimerRecord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TimerRecordDataStore(private val dataStore: DataStore<Preferences>) {

    private val recordsKey = stringPreferencesKey("pending_timer_records")
    private val gson = Gson()
    private val listType = object : TypeToken<List<TimerRecord>>() {}.type

    suspend fun append(record: TimerRecord) {
        dataStore.edit { preferences ->
            val current: List<TimerRecord> = preferences[recordsKey]?.let {
                gson.fromJson(it, listType)
            } ?: emptyList()
            preferences[recordsKey] = gson.toJson(current + record, listType)
        }
    }

    suspend fun getAll(): List<TimerRecord> {
        return dataStore.data.map { preferences ->
            val stored: List<TimerRecord>? = preferences[recordsKey]?.let {
                gson.fromJson(it, listType)
            }
            stored ?: emptyList()
        }.first()
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(recordsKey)
        }
    }
}
