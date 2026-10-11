package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.model.CalendarMonth
import com.ggaebiz.ggaebiz.domain.repository.StatisticRepository

class GetCalendarUseCase(private val repository: StatisticRepository) {
    suspend operator fun invoke(yearMonth: String): Result<List<CalendarMonth>> =
        repository.getCalendar(yearMonth)
}
