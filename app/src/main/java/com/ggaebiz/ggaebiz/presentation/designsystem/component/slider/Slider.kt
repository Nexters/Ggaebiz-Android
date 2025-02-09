package com.ggaebiz.ggaebiz.presentation.designsystem.component.slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import kotlin.math.roundToInt

@Composable
fun GaeBizSlider(
    modifier: Modifier = Modifier,
    initialLevel: Int,
    onValueChange: (Int) -> Unit,
    maxLevel: Int,
    thumbSize: Dp,
    thumbColor: Color,
    trackHeight: Dp,
    activeTrackColor: Color,
    inactiveTrackColor: Color,
) {
    var selectedLevel by remember { mutableStateOf(initialLevel - 1) }
    var sliderWidth by remember { mutableStateOf(0f) }

    val density = LocalDensity.current
    val levelSpacing = if (maxLevel > 1) sliderWidth / (maxLevel - 1) else 0f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val newLevel = ((change.position.x / sliderWidth) * (maxLevel - 1))
                        .roundToInt()
                        .coerceIn(0, maxLevel - 1)

                    if (newLevel != selectedLevel) {
                        selectedLevel = newLevel
                        onValueChange(newLevel + 1)
                    }
                }
            }
            .onSizeChanged { size ->
                sliderWidth = size.width.toFloat()
            },
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.CenterStart),
        ) {
            drawRoundRect(
                color = inactiveTrackColor,
                size = Size(size.width, trackHeight.toPx()),
                cornerRadius = CornerRadius(trackHeight.toPx() / 2),
            )

            val activeWidth = selectedLevel * levelSpacing
            drawRoundRect(
                color = activeTrackColor,
                size = Size(activeWidth, trackHeight.toPx()),
                cornerRadius = CornerRadius(trackHeight.toPx() / 2),
            )
        }

        val thumbX = with(density) { (selectedLevel * levelSpacing).toDp() }
        Canvas(
            modifier = Modifier
                .offset(x = thumbX - (thumbSize / 2))
                .size(thumbSize),
        ) {
            drawCircle(
                color = thumbColor,
                radius = size.minDimension / 2,
            )
        }
    }
}

@Preview("Slider")
@Composable
private fun GaeBizSliderPreview() {
    GaeBizSlider(
        modifier = Modifier.fillMaxWidth(),
        maxLevel = 3,
        initialLevel = 2,
        onValueChange = { },
        thumbSize = 20.dp,
        thumbColor = GaeBizTheme.colors.gray900,
        trackHeight = 4.dp,
        activeTrackColor = GaeBizTheme.colors.gray900,
        inactiveTrackColor = GaeBizTheme.colors.gray75,
    )
}
