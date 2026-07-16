package com.ggaebiz.ggaebiz.data.repository

import android.util.Log
import com.ggaebiz.ggaebiz.data.datastore.TimerRecordDataStore
import com.ggaebiz.ggaebiz.data.network.TimerApi
import com.ggaebiz.ggaebiz.data.network.dto.toRequest
import com.ggaebiz.ggaebiz.domain.model.TimerRecord
import com.ggaebiz.ggaebiz.domain.repository.TimerRecordRepository

class TimerRecordRepositoryImpl(
    private val timerRecordDataStore: TimerRecordDataStore,
    private val timerApi: TimerApi,
) : TimerRecordRepository {

    companion object {
        private const val TAG = "TimerRecordRepository"
    }

    override suspend fun appendRecord(record: TimerRecord) {
        try {
            timerRecordDataStore.append(record)
        } catch (e: Exception) {
            Log.e(TAG, "Error appending timer record", e)
        }
    }

    override suspend fun sendPendingRecords(): Result<Unit> {
        return try {
            val pending = timerRecordDataStore.getAll()
            if (pending.isEmpty()) return Result.success(Unit)

            val response = timerApi.sendTimerRecords(pending.map { it.toRequest() })
            if (response.isSuccessful) {
                // TODO: 전송 진행 중 새 기록이 append 되면 함께 삭제될 수 있음(희귀). 필요 시 전송분만 선별 삭제로 개선.
                timerRecordDataStore.clear()
                Result.success(Unit)
            } else {
                Result.failure(IllegalStateException("타이머 기록 전송 실패 (${response.code()})"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending timer records", e)
            Result.failure(e)
        }
    }
}
