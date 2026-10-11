package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.model.TopCardData
import com.ggaebiz.ggaebiz.domain.repository.StatisticRepository

class GetTopCardDataUseCase(private val repository: StatisticRepository) {
    suspend operator fun invoke(): Result<TopCardData> {
        val info = repository.getTopCardInfo().getOrElse { return Result.failure(it) }
        val frequency = repository.getCharacterFrequency().getOrDefault(emptyList())
        return Result.success(
            TopCardData(
                streakDays = info.streakDays,
                lastAttendanceDate = info.lastAttendanceDate,
                selectionCountList = frequency,
            )
        )
    }
}
