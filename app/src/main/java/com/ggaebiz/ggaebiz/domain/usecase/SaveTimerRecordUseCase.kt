package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.model.TimerRecord
import com.ggaebiz.ggaebiz.domain.repository.TimerRecordRepository

class SaveTimerRecordUseCase(private val repository: TimerRecordRepository) {
    suspend operator fun invoke(record: TimerRecord) = repository.appendRecord(record)
}
