package com.ggaebiz.ggaebiz.presentation.ui.alarm

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.FullScreen
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
import com.ggaebiz.ggaebiz.presentation.ui.setting.TimerMode
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@Composable
fun AlarmScreen(
    navigateStart: () -> Unit,
    navigateTimer: () -> Unit,
    viewModel: AlarmViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val timerServiceManager: TimerServiceManager by getKoin().inject()

    DisposableEffect(Unit) {
        onDispose {
            timerServiceManager.unbindOverCountService()
        }
    }
    BackHandler(enabled = true) { }

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            is AlarmSideEffect.ClickFinish -> {
                timerServiceManager.stopTimerService()
                navigateStart()
            }
            is AlarmSideEffect.ClickSnooze -> {
                timerServiceManager.stopTimerService()
                navigateTimer()
            }
            is AlarmSideEffect.GetOverCount -> {
                timerServiceManager.bindOverCountService { serviceFlow ->
                    Log.d("TimerService","LaunchedEffect :: ${serviceFlow.value}")
                    viewModel.setTimer(serviceFlow)
                    viewModel.processIntent(AlarmIntent.StartOverCount)
                }
            }
        }

    }
    AlarmContent(
        uiState = uiState,
        processIntent = viewModel::processIntent,
    )
}

@Composable
fun AlarmContent(
    uiState: AlarmState,
    processIntent: (AlarmIntent) -> Unit,
) {
    FullScreen(
        backGroundImage = uiState.backGroundImgRes
    ) {
        AlarmTopSection(
            ment = uiState.ment, plusSecond = uiState.plusSeconds
        )
        AlarmBottomSection(
            finishButtonTextRes = getFinishButtonTextRes(uiState.timerMode, uiState.isRestAvailable),
            snoozeButtonTextRes = getSnoozeButtonTextRes(uiState.timerMode, uiState.isRestAvailable, uiState.snoozeCount),
            onClickFinishButton = { finishButtonClickEvent(uiState.timerMode, uiState.isRestAvailable, processIntent) },
            onClickSnoozeButton = { snoozeButtonClickEvent(uiState.snoozeCount, processIntent) },
            isDisableSnoozeButton = uiState.disableSnoozeButton
        )
    }
}

@StringRes
fun getFinishButtonTextRes(timerMode: TimerMode, isRestAvailable: Boolean): Int =
    when {
        isRestAvailable -> R.string.alarm_finish_timer_btn_text
        timerMode.isConcentrateTimer() -> R.string.alarm_resume_timer_btn_text
        else -> R.string.alarm_finish_timer_btn_text
    }

@StringRes
fun getSnoozeButtonTextRes(timerMode: TimerMode, isRestAvailable: Boolean, snoozeCount: Int): Int =
    when {
        !timerMode.isConcentrateTimer() -> R.string.alarm_snooze_timer_btn_text
        snoozeCount >= 2 -> R.string.alarm_finish_timer_btn_text
        isRestAvailable -> R.string.alarm_resume_after_10_minutes_timer_btn_text
        else -> R.string.alarm_snooze_timer_btn_text
    }

fun finishButtonClickEvent(timerMode: TimerMode, isRestAvailable: Boolean, processIntent: (AlarmIntent) -> Unit) =
    when {
        isRestAvailable -> processIntent(AlarmIntent.ClickFinish)
        timerMode.isConcentrateTimer() -> processIntent(AlarmIntent.ClickResumeConcentrate)
        else -> processIntent(AlarmIntent.ClickFinish)
    }

fun snoozeButtonClickEvent(snoozeCount: Int, processIntent: (AlarmIntent) -> Unit) =
    when {
        snoozeCount >= 2 -> processIntent(AlarmIntent.ClickFinish)
        else -> processIntent(AlarmIntent.ClickSnooze)
    }

@Preview(showBackground = true)
@Composable
fun AlarmScreenPreview() {
    GaeBizTheme {
        AlarmScreen({}, {})
    }
}
