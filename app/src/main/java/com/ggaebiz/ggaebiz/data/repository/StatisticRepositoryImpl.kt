package com.ggaebiz.ggaebiz.data.repository

import android.util.Log
import com.ggaebiz.ggaebiz.data.network.StatisticApi
import com.ggaebiz.ggaebiz.domain.model.CalendarMonth
import com.ggaebiz.ggaebiz.domain.model.TimerTimeRecord
import com.ggaebiz.ggaebiz.domain.model.TopCardInfo
import com.ggaebiz.ggaebiz.domain.repository.StatisticRepository

class StatisticRepositoryImpl(
    private val statisticApi: StatisticApi,
) : StatisticRepository {

    companion object {
        private const val TAG = "StatisticRepository"

        private fun parseFeverDay(raw: String?): Int? =
            raw?.substringAfterLast('-')?.trim()?.toIntOrNull()
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

    override suspend fun getTimerTimes(): Result<List<TimerTimeRecord>> = try {
        val records = statisticApi.getTimerTimes().result.map {
            TimerTimeRecord(
                mode = it.mode,
                concentrateType = it.concentrateType,
                timeType = it.timeType,
                time = it.time,
            )
        }
        Result.success(records)
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching timer times", e)
        Result.failure(e)
    }

    override suspend fun getCalendar(yearMonth: String): Result<List<CalendarMonth>> = try {
        val months = statisticApi.getCalendar(yearMonth).result.map {
            CalendarMonth(
                yearMonth = it.playRecord.yearMonth,
                dayRecord = it.playRecord.dayRecord,
                feverDay = parseFeverDay(it.feverDay),
            )
        }
        Result.success(months)
    } catch (e: Exception) {
        Log.e(TAG, "Error fetching calendar", e)
        Result.failure(e)
    }

}
