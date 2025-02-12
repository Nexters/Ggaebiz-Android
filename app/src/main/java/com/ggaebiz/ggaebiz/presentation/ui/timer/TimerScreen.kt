package com.ggaebiz.ggaebiz.presentation.ui.timer

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizLogoAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.component.timer.GaeBizTimer
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizMent
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizSlideButton
import com.ggaebiz.ggaebiz.presentation.model.Character.Companion.CHARACTER_LIST
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin


@Composable
fun TimerScreen(
    viewModel: TimerViewModel = koinViewModel(),
    navigateHome: () -> Unit = {},
    navigateAlarm: () -> Unit = {},
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val timerServiceManager: TimerServiceManager by getKoin().inject()

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            is TimerSideEffect.ShowToast -> showToast(context, uiState)
            is TimerSideEffect.StartService -> {
                timerServiceManager.startTimerService(effect.seconds, effect.audioResPath)
            }

            is TimerSideEffect.StopService -> {
                timerServiceManager.stopTimerService()
                navigateHome()
            }
        }
    }

    TimerContent(
        uiState = uiState,
        processIntent = viewModel::processIntent,
    )
}

@Composable
fun TimerContent(
    modifier: Modifier = Modifier,
    uiState: TimerState,
    processIntent: (TimerIntent) -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageWidth = screenWidth / 3 * 2

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GaeBizLogoAppBar()

        Spacer(modifier = Modifier.height(58.dp))
        GaeBizMent(
            text = stringResource(CHARACTER_LIST[uiState.selectedCharacterIdx].timerMentResId),
        )

        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(CHARACTER_LIST[uiState.selectedCharacterIdx].imageResId[uiState.level - 1]),
            contentDescription = null,
            modifier = Modifier.size(imageWidth),
            contentScale = ContentScale.Crop,
        )

        GaeBizTimer(remainingSeconds = uiState.remainingSeconds)

        Spacer(modifier = Modifier.weight(1f))
        GaeBizSlideButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 58.dp, end = 58.dp, bottom = 96.dp),
            text = stringResource(R.string.end_button_text),
            onSlideComplete = { processIntent(TimerIntent.StopTimer) },
        )
    }
}

fun showToast(context: Context, uiState: TimerState) {
    if (uiState.remainingSeconds >= 3600) {
        Toast.makeText(
            context,
            context.getString(
                R.string.timer_time_minute_toast_text,
                context.getString(CHARACTER_LIST[uiState.selectedCharacterIdx].nameResId),
                uiState.hour,
                uiState.minute
            ),
            LENGTH_SHORT,
        ).show()
    } else {
        Toast.makeText(
            context,
            context.getString(
                R.string.timer_minute_toast_text,
                context.getString(CHARACTER_LIST[uiState.selectedCharacterIdx].nameResId),
                uiState.minute,
            ),
            LENGTH_SHORT,
        ).show()
    }
}
