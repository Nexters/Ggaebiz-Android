package com.ggaebiz.ggaebiz.domain.usecase

import com.ggaebiz.ggaebiz.domain.repository.TimerRepository
import com.ggaebiz.ggaebiz.presentation.ui.home.Quadruple
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode

class GetCurrentTimerUseCase(private val repository: TimerRepository) {
    suspend operator fun invoke(): Quadruple<Int, Int, Int, TimerMode> {
        repository.run {
            return Quadruple(getLevel(), getHour(), getMinute(), getTimerMode())
        }
    }

    suspend fun getLevelIdx(): Int {
        repository.run {
            return getLevelIdx()
        }
    }
}
