package com.ggaebiz.ggaebiz.presentation.ui.home

import GaeBizPopupButton
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.popup.ImagePopup
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.popup.ListPopup
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.popup.TextPopup
import com.ggaebiz.ggaebiz.presentation.model.Character
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun NudgeGuideComponent(
    imgWidth: Dp,
    guideIdx: Int,
    clickSkipButton: () -> Unit,
    clickConfirmButton: () -> Unit,
) {
    val (imgRes, idxText, bottomBlank, btnText) = when (guideIdx) {
        1 -> Quadruple(R.drawable.home_nudge1, "1/2", 32f, "다음")
        else -> Quadruple(R.drawable.home_nudge2, "2/2", 100f, "확인")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background((GaeBizTheme.colors.black).copy(alpha = 0.8f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = idxText,
                color = GaeBizTheme.colors.white,
                style = GaeBizTheme.typography.bodySemiBold
            )
        }
        Spacer(modifier = Modifier.weight(56f))
        Image(
            painter = painterResource(imgRes),
            contentDescription = null,
            modifier = Modifier.width(imgWidth),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.weight(bottomBlank))
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            if (guideIdx == 1) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { clickSkipButton() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "건너뛰기",
                        style = GaeBizTheme.typography.bodyMedium,
                        color = GaeBizTheme.colors.gray100,
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
            GaeBizButton(
                modifier = Modifier.fillMaxWidth(),
                text = btnText,
                style = GaeBizTheme.typography.bodySemiBold,
                containerColor = GaeBizTheme.colors.primaryOrange,
                contentColor = GaeBizTheme.colors.white,
                onClick = { clickConfirmButton() }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}


@Composable
fun AnimatedCharacterItem(
    character: Character,
    imageWidth: Dp,
    exoPlayer: ExoPlayer,
    isActive: Boolean,
    playMent: () -> Unit,
    enabled: Boolean,
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val scale by animateFloatAsState(
        targetValue = if (isPressed || isAnimating) 0.9f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow), label = ""
    )

    val soundUri =
        Uri.parse("android.resource://${context.packageName}/${character.initMentAudioResId}")

    LaunchedEffect(isActive) {
        if (!isActive) {
            exoPlayer.pause()
            isPlaying = false
        }
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    isPlaying = false
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    fun handleTap() {
        if (isPlaying) {
            exoPlayer.pause()
            isPlaying = false
        } else {
            playMent.invoke()
            exoPlayer.setMediaItem(MediaItem.fromUri(soundUri))
            exoPlayer.prepare()
            exoPlayer.seekTo(0)
            exoPlayer.play()
            isPlaying = true
        }
    }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        if (!enabled) return@detectTapGestures
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        if (!enabled) return@detectTapGestures
                        scope.launch {
                            isAnimating = true
                            delay(250)
                            isAnimating = false
                        }
                        handleTap()
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = character.imageResId[0]),
            contentDescription = null,
            modifier = Modifier.size(imageWidth),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun BatteryPopup(
    visible: Boolean,
    onClickOk : () -> Unit,
    onClickDenied : () -> Unit
){
    TextPopup(
        visible = visible,
        titleText = stringResource(R.string.home_batter_popup_title),
        bodyText = stringResource(R.string.home_battery_popup_content),
        position = GaeBizPopupPosition.Center,
        buttons = listOf(
            GaeBizPopupButton(
                text = stringResource(R.string.home_battery_next_button),
                style = GaeBizButtonStyle.Secondary,
                onClick = { onClickDenied()}
            ),
            GaeBizPopupButton(
                text = stringResource(R.string.home_battery_move_button),
                style = GaeBizButtonStyle.Primary,
                onClick = { onClickOk() }
            )
        )
    )
}


@Composable
fun ProofPopup(
    visible: Boolean,
    onClickOk : () -> Unit,
    onClickDenied : () -> Unit
){
    ImagePopup(
        visible = visible,
        titleText = stringResource(R.string.home_proof_popup_title),
        subtitleText = stringResource(R.string.home_proof_popup_subtitle),
        image = painterResource( R.drawable.img_proof_popup),
        position = GaeBizPopupPosition.Bottom,
        contentScale = ContentScale.FillHeight,
        buttons = listOf(
            GaeBizPopupButton(
                text = stringResource(R.string.home_proof_popup_denied_button),
                style = GaeBizButtonStyle.Secondary,
                onClick = { onClickDenied()}
            ),
            GaeBizPopupButton(
                text = stringResource(R.string.home_proof_popup_ok_button),
                style = GaeBizButtonStyle.Primary,
                onClick = {onClickOk()}
            )
        )
    )
}


@Composable
fun ListPopupItem(
    text: String,
    onClick: () -> Unit,
    leadingIcon: ImageVector? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val bgTarget = if (hovered) GaeBizTheme.colors.gray50 else GaeBizTheme.colors.white
    val bg by animateColorAsState(bgTarget, label = "bg")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(bg)
            .hoverable(interaction)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp),
                tint = Color.Unspecified
            )
        }
        Text(
            text = text,
            color = GaeBizTheme.colors.gray900,
            style = GaeBizTheme.typography.bodySemiBold,
            modifier = Modifier.weight(1f)
        )

        if (hovered) {
            Icon(
                imageVector = GaeBizIcon.icFillCheck,
                contentDescription = null,
                tint = GaeBizTheme.colors.primaryOrange
            )
        }
    }
}

@Composable
fun ChoiceWayPopup(
    visible : Boolean,
    onClickCamera : () -> Unit,
    onClickGallery : () -> Unit,
    onClickAlbum : () -> Unit
){
    ListPopup(
        visible = visible,
        titleText = stringResource(R.string.home_choice_way_popup_title),
        position = GaeBizPopupPosition.Bottom,
        itemContent = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ListPopupItem(
                    text = stringResource(R.string.home_choice_way_camera),
                    onClick = {onClickCamera()},
                    leadingIcon = GaeBizIcon.icProofCamera,
                )
                ListPopupItem(
                    text = stringResource(R.string.home_choice_way_album),
                    onClick = {onClickGallery()},
                    leadingIcon = GaeBizIcon.icProofAlbum,
                )
                ListPopupItem(
                    text = stringResource(R.string.home_choice_way_card),
                    onClick = {onClickAlbum()},
                    leadingIcon = GaeBizIcon.icProofCard,
                )
            }
        }
    )
}