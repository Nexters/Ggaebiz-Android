package com.ggaebiz.ggaebiz.presentation.ui.alarm

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
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.getRawResId
import org.koin.androidx.compose.koinViewModel


@Composable
fun AlarmScreen(viewModel: AlarmViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            is AlarmSideEffect.PlayAudio -> {
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

    Button(
        modifier = Modifier.systemBarsPadding(),
        onClick = { viewModel.startTimer(Character.KIKI, 3) }
    ) {
        Text(uiState.text)
    }
}