package com.ggaebiz.ggaebiz.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OnboardingDataStore(private val dataStore: DataStore<Preferences>) {

    private val isOnboardingExposed = booleanPreferencesKey("is_onboarding_exposed")

    companion object {
        const val DEFAULT_IS_ONBOARDING_EXPOSED = false
    }

    suspend fun setIsOnboardingExposed(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[isOnboardingExposed] = value
        }
    }

    fun getIsOnboardingExposed(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isOnboardingExposed] ?: DEFAULT_IS_ONBOARDING_EXPOSED
        }
    }
}
