package com.ggaebiz.ggaebiz.presentation.ui.timer

import android.net.Uri
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.ggaebiz.ggaebiz.data.model.Character
import org.koin.androidx.compose.koinViewModel


@Composable
fun TimerScreen(viewModel: TimerViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is TimerSideEffect.PlayAudio -> {
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
            }
        }
    }
    Button(
        modifier = Modifier.systemBarsPadding(),
        onClick = { viewModel.startTimer(Character.KIKI, 3) }
    ) {
        Text(uiState.text)
    }
}