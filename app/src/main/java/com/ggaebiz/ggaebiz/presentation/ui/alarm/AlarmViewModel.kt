package com.ggaebiz.ggaebiz.presentation.ui.alarm

import android.util.Log
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.domain.usecase.GetCharacterIdxUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetTimerSettingUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetSnoozeCountUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SetTimerSettingUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import kotlinx.coroutines.delay
import java.util.Locale

data class AlarmState(
    val ment: String = "A ~~ yo ! ! !  계획만 천재야, 행동은 문제야, 일어나지 않으면 오늘도 넌 패배야",
    val backGroundImgRes: Int = R.drawable.fullpage_kiki_lev_1,
    val plusSeconds: String = "+ 00:00",
    var snoozeCount: Int = 0,
) {
    val disableSnoozeButton: Boolean = snoozeCount >= 2
}

sealed interface AlarmIntent {
    data object ClickSnooze : AlarmIntent
    data object ClickFinish : AlarmIntent
}

sealed interface AlarmSideEffect {
    data object ClickSnooze : AlarmSideEffect
    data object ClickFinish : AlarmSideEffect
}

class AlarmViewModel(
    private val getCharacterIdxUseCase: GetCharacterIdxUseCase,
    private val getTimerSettingUseCase: GetTimerSettingUseCase,
    private val setTimerSettingUseCase: SetTimerSettingUseCase,
    private val getSnoozeCountUseCase: GetSnoozeCountUseCase,
    private val setSnoozeCountUseCase: SetSnoozeCountUseCase,
) : BaseViewModel<AlarmState, Nothing, AlarmSideEffect>(AlarmState()) {

    init {
        startIncreaseSeconds()
        getTimerInfo()
    }

    fun processIntent(intent: AlarmIntent) {
        when (intent) {
            AlarmIntent.ClickFinish -> finishTimer()
            AlarmIntent.ClickSnooze -> snoozeTimer()
        }
    }

    private fun startIncreaseSeconds() = launch {
        while (true) {
            delay(1000L) // 1초 대기
            updateState {
                val (minute, second) = parseTime(it.plusSeconds)
                val newSecond = second + 1
                val newPlusSeconds = formatTime(newSecond, minute)
                it.copy(plusSeconds = newPlusSeconds)
            }
        }
    }

    private fun getTimerInfo() = launch {
        val snoozeCount = getSnoozeCountUseCase()
        Log.d("VIewModel", "현재 스누즈 카운트 : ${snoozeCount}")

        updateState { it.copy(snoozeCount = snoozeCount) }
        // TODO :: 멘트/음원 데이터 가져오기 구현
    }

    private fun finishTimer() = launch {
        setSnoozeCountUseCase.invoke(0)
        postSideEffect(AlarmSideEffect.ClickFinish)
    }

    private fun snoozeTimer() = launch {
        setSnoozeCountUseCase.invoke((getSnoozeCountUseCase()) + 1)
        val level = getTimerSettingUseCase().first

        setTimerSettingUseCase(
            level = level + 1,
            hour = 0,
            minute = 5,
            snoozeCount = (uiState.value.snoozeCount) + 1
        )
        postSideEffect(AlarmSideEffect.ClickSnooze)
    }

    private fun formatTime(seconds: Int, minute: Int): String {
        val newMinute = if (seconds >= 60) minute + 1 else minute
        val newSecondFormatted = seconds % 60
        return String.format(Locale.KOREA, "+ %02d:%02d", newMinute, newSecondFormatted)
    }

    private fun parseTime(time: String): Pair<Int, Int> {
        val regex = "\\+ (\\d{2}):(\\d{2})".toRegex()
        val matchResult = regex.find(time)
        return if (matchResult != null) {
            val minute = matchResult.groupValues[1].toInt()
            val second = matchResult.groupValues[2].toInt()
            Pair(minute, second)
        } else {
            Pair(0, 0) // 기본값
        }
    }

}
