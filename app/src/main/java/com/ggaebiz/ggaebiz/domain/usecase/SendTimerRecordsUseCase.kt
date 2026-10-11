package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRecordRepository

class SendTimerRecordsUseCase(private val repository: TimerRecordRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.sendPendingRecords()
}
