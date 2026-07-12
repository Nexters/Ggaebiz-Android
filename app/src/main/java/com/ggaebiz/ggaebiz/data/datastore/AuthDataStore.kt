package com.ggaebiz.ggaebiz.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class AuthDataStore(private val dataStore: DataStore<Preferences>) {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val userIdKey = intPreferencesKey("user_id")
    private val nicknameKey = stringPreferencesKey("nickname")

    suspend fun saveAuthInfo(accessToken: String, userId: Int) {
        dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
            preferences[userIdKey] = userId
        }
    }

    suspend fun clearAuthInfo() {
        dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(userIdKey)
            preferences.remove(nicknameKey)
        }
    }

    suspend fun saveNickname(nickname: String) {
        dataStore.edit { preferences ->
            preferences[nicknameKey] = nickname
        }
    }

    fun getNickname(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[nicknameKey]
        }
    }

    fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[accessTokenKey]
        }
    }

    fun getUserId(): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[userIdKey]
        }
    }

    suspend fun isLoggedIn(): Boolean {
        val accessToken = dataStore.data.map { it[accessTokenKey] }.firstOrNull()
        val userId = dataStore.data.map { it[userIdKey] }.firstOrNull()
        return !accessToken.isNullOrEmpty() && userId != null
    }
}
