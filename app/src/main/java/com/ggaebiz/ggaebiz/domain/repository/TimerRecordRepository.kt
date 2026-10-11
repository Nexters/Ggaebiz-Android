package com.ggaebiz.ggaebiz.domain.repository

import com.ggaebiz.ggaebiz.domain.model.TimerRecord

interface TimerRecordRepository {
    suspend fun appendRecord(record: TimerRecord)
    suspend fun sendPendingRecords(): Result<Unit>
}
