package com.ggaebiz.ggaebiz.data.repository

import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore.Companion.DEFAULT_IS_VIBRATION
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore.Companion.DEFAULT_VIBRATION
import com.ggaebiz.ggaebiz.data.datastore.ConfigDataStore.Companion.DEFAULT_VOLUME
import com.ggaebiz.ggaebiz.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.firstOrNull

class ConfigRepositoryImpl(
    private val configDataStore: ConfigDataStore,
) : ConfigRepository {
    override suspend fun setVibrationStatus(value: Boolean) =
        configDataStore.setVibrationStatus(value)

    override suspend fun getVibrationStatus(): Boolean =
        configDataStore.getVibrationStatus().firstOrNull() ?: DEFAULT_IS_VIBRATION

    override suspend fun setVibrationValue(value: Int) = configDataStore.setVibrationValue(value)
    override suspend fun getVibrationValue(): Int =
        configDataStore.getVibrationValue().firstOrNull() ?: DEFAULT_VIBRATION

    override suspend fun setVolumeValue(value: Int) = configDataStore.setVolumeValue(value)

    override suspend fun getVolumeValue(): Int =
        configDataStore.getVolumeValue().firstOrNull() ?: DEFAULT_VOLUME
}