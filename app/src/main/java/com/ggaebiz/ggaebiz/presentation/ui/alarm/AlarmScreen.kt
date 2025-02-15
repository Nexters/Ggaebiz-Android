package com.ggaebiz.ggaebiz.presentation.ui.alarm

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.FullScreen
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
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
            onClickFinishButton = { processIntent(AlarmIntent.ClickFinish) },
            onClickSnoozeButton = { processIntent(AlarmIntent.ClickSnooze) },
            isDisableSnoozeButton = uiState.disableSnoozeButton
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmScreenPreview() {
    GaeBizTheme {
        AlarmScreen({}, {})
    }
}
