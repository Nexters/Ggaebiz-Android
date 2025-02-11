package com.ggaebiz.ggaebiz.presentation.ui.alarm

import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.ggaebiz.ggaebiz.domain.usecase.GetAudioResIdUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import kotlinx.coroutines.delay
import java.util.Locale

data class AlarmState(
    // TODO :: ment, image 연동
    val ment: String = "A ~~ yo ! ! !  계획만 천재야, 행동은 문제야, 일어나지 않으면 오늘도 넌 패배야",
    val plusSeconds: String = "+ 00:00",
    val backGroundImgRes: Int = R.drawable.fullpage_kiki_lev_1,
)

sealed interface AlarmSideEffect {
    data class PlayAudio(val resId: String) : AlarmSideEffect
    data object ClickSnooze : AlarmSideEffect
    data object ClickFinish : AlarmSideEffect
}

class AlarmViewModel(
    private val getAudioResIdUseCase: GetAudioResIdUseCase,
) : BaseViewModel<AlarmState, Nothing, AlarmSideEffect>(AlarmState()) {

    init {
        startIncreaseSeconds()
    }

    fun startTimer(
        characterName: CharacterName,
        level: Int,
    ) = launch {
        val resIds = getAudioResIdUseCase(characterName, level, 0)
        updateState { it.copy(ment = "타이머 울리는 중") }
        postSideEffect(AlarmSideEffect.PlayAudio(resIds))
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

    fun finishTimer() {
        postSideEffect(AlarmSideEffect.ClickFinish)
    }

    fun snoozeTimer() {
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
