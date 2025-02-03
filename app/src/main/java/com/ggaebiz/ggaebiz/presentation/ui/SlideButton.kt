package com.ggaebiz.ggaebiz.presentation.ui

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import kotlinx.coroutines.launch

@Composable
fun GaeBizSlideButton(
    modifier: Modifier = Modifier,
    text: String,
    onSlideComplete: () -> Unit,
    slideIcon: ImageVector = GaeBizIcon.icRightArrow,
    iconColor: Color = GaeBizTheme.colors.white,
) {
    var buttonState by remember { mutableStateOf(SliderButtonState.ENABLE) }

    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = stringResource(R.string.slider_anim_label)
    )

    val coroutineScope = rememberCoroutineScope()
    var buttonWidthPx by remember { mutableStateOf(0f) }

    val iconSize = 56.dp
    val iconInnerPadding = 8.dp
    val iconOuterPadding = 5.dp
    val maxDragDistance = with(LocalDensity.current) {
        buttonWidthPx - (iconSize + iconInnerPadding * 2 - iconOuterPadding).toPx()
    }

    val handleColor = when (buttonState) {
        SliderButtonState.ENABLE -> GaeBizTheme.colors.gray800
        SliderButtonState.PRESSED, SliderButtonState.DRAGGED -> GaeBizTheme.colors.gray900
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(50))
            .background(GaeBizTheme.colors.gray50)
            .border(1.dp, GaeBizTheme.colors.gray75, RoundedCornerShape(50))
            .onSizeChanged { size ->
                buttonWidthPx = size.width.toFloat()
            }
            .padding(5.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(with(LocalDensity.current) { animatedOffsetX.toDp() } + iconSize)
                .clip(CircleShape)
                .background(GaeBizTheme.colors.gray900),
        )

        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(1f - (offsetX / maxDragDistance)),
            color = GaeBizTheme.colors.gray500,
            style = GaeBizTheme.typography.bodySemiBold,
        )

        Box(
            modifier = Modifier
                .height(iconSize)
                .width(with(LocalDensity.current) { animatedOffsetX.toDp() + iconSize })
                .clip(CircleShape)
                .background(handleColor)
                .offset { IntOffset((animatedOffsetX).toInt(), 0) }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            buttonState = SliderButtonState.PRESSED
                            buttonState = SliderButtonState.ENABLE
                        }
                    )
                }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX = (offsetX + delta).coerceIn(0f, maxDragDistance)
                    },
                    onDragStarted = {
                        buttonState = SliderButtonState.DRAGGED
                    },
                    onDragStopped = {
                        if (offsetX >= maxDragDistance * 0.8f) {
                            offsetX = maxDragDistance
                            onSlideComplete()
                        } else {
                            coroutineScope.launch {
                                animate(
                                    initialValue = offsetX,
                                    targetValue = 0f,
                                    animationSpec = tween(
                                        durationMillis = 600,
                                        easing = EaseOutCubic,
                                    )
                                ) { value, _ ->
                                    offsetX = value
                                }
                                buttonState = SliderButtonState.ENABLE
                            }
                        }
                    }
                ),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier.size(iconSize),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = slideIcon,
                    contentDescription = stringResource(R.string.slide_icon_description),
                    tint = iconColor,
                )
            }
        }
    }
}

enum class SliderButtonState {
    ENABLE, PRESSED, DRAGGED
}

@Preview
@Composable
fun GaeBizSlideButtonPreview() {
    GaeBizSlideButton(
        text = stringResource(R.string.setting_title_text),
        onSlideComplete = { },
    )
}
