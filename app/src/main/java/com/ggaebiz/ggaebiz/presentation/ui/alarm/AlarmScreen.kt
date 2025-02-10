package com.ggaebiz.ggaebiz.presentation.ui.alarm

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.getRawResId
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.FullScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlarmScreen(
    navigateStart: () -> Unit,
    viewModel: AlarmViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            is AlarmSideEffect.PlayAudio -> {
                // TODO :: 분리 필요
                ExoPlayer.Builder(context).build().apply {
                    val resInt = context.getRawResId(effect.resId)
                    val mediaItem = MediaItem.fromUri(
                        Uri.parse("android.resource://${context.packageName}/${resInt}")
                    )
                    setMediaItem(mediaItem)
                    prepare()
                    play()

                }
            }
            AlarmSideEffect.ClickFinish -> {
                navigateStart()
            }
            AlarmSideEffect.ClickSnooze -> {
                navigateStart()
            }
        }
    }
    AlarmContent(
        uiState = uiState,
        onClickFinishButton = { viewModel.finishTimer() },
        onClickSnoozeButton = { viewModel.snoozeTimer() }
    )
}


@Composable
fun AlarmContent(
    uiState: AlarmState,
    onClickFinishButton: () -> Unit,
    onClickSnoozeButton: () -> Unit,
) {
    FullScreen(
        backGroundImage = uiState.backGroundImgRes
    ) {
        AlarmTopSection(
            ment = uiState.ment,
            plusSecond = uiState.plusSeconds
        )
        AlarmBottomSection(
            onClickFinishButton = onClickFinishButton,
            onClickSnoozeButton = onClickSnoozeButton,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmScreenPreview() {
    GaeBizTheme {
        AlarmScreen({})
    }
}