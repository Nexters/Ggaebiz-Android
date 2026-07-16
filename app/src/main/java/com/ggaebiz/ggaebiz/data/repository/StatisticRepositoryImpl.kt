package com.ggaebiz.ggaebiz.data.repository

import android.util.Log
import com.ggaebiz.ggaebiz.data.network.StatisticApi
import com.ggaebiz.ggaebiz.domain.model.TopCardInfo
import com.ggaebiz.ggaebiz.domain.repository.StatisticRepository

class StatisticRepositoryImpl(
    private val statisticApi: StatisticApi,
) : StatisticRepository {

    companion object {
        private const val TAG = "StatisticRepository"
    }

    override suspend fun getTopCardInfo(): Result<TopCardInfo> = try {
        val response = statisticApi.getTopCard()
        Result.success(TopCardInfo(response.streakDays, response.lastAttendanceDate))
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching top card", e)
        Result.failure(e)
    }

    override suspend fun getCharacterFrequency(): Result<List<Int>> = try {
        Result.success(statisticApi.getCharacterFrequency().selectionCountList)
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching character frequency", e)
        Result.failure(e)
    }
}
