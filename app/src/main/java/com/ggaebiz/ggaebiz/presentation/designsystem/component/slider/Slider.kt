package com.ggaebiz.ggaebiz.presentation.designsystem.component.slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    var isDragging by remember { mutableStateOf(false) }
    var dragPosition by remember { mutableStateOf(0f) }

    val density = LocalDensity.current
    val levelSpacing = if (maxLevel > 1) sliderWidth / (maxLevel - 1) else 0f

    LaunchedEffect(initialLevel) {
        selectedLevel = initialLevel - 1
        dragPosition = selectedLevel * levelSpacing
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .pointerInput(maxLevel, sliderWidth) {
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        dragPosition = offset.x.coerceIn(0f, sliderWidth)
                    },
                    onDrag = { change, _ ->
                        change.consume()

                        isDragging = true
                        dragPosition = change.position.x.coerceIn(0f, sliderWidth)
                    },
                    onDragEnd = {
                        val newLevel = ((dragPosition / sliderWidth) * (maxLevel - 1))
                            .roundToInt()
                            .coerceIn(0, maxLevel - 1)

                        selectedLevel = newLevel
                        dragPosition = selectedLevel * levelSpacing

                        onValueChange(newLevel + 1)

                        isDragging = false
                    },
                    onDragCancel = {
                        dragPosition = selectedLevel * levelSpacing
                        isDragging = false
                    }
                )
            }
            .pointerInput(maxLevel, sliderWidth) {
                detectTapGestures { offset ->
                    val newLevel = ((offset.x / sliderWidth) * (maxLevel - 1))
                        .roundToInt()
                        .coerceIn(0, maxLevel - 1)

                    selectedLevel = newLevel
                    dragPosition = selectedLevel * levelSpacing
                    onValueChange(newLevel + 1)
                }
            }
            .onSizeChanged { size ->
                sliderWidth = size.width.toFloat()
                dragPosition = selectedLevel * levelSpacing
            },
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(trackHeight)
                .align(Alignment.CenterStart),
        ) {
            drawRoundRect(
                color = inactiveTrackColor,
                size = Size(size.width, trackHeight.toPx()),
                cornerRadius = CornerRadius(trackHeight.toPx() / 2),
            )

            val activeWidth = if (isDragging) dragPosition else selectedLevel * levelSpacing
            drawRoundRect(
                color = activeTrackColor,
                size = Size(activeWidth, trackHeight.toPx()),
                cornerRadius = CornerRadius(trackHeight.toPx() / 2),
            )
        }

        val thumbX = with(density) {
            val px = if (isDragging) dragPosition else selectedLevel * levelSpacing
            px.toDp()
        }
        Canvas(
            modifier = Modifier
                .offset(x = thumbX - (thumbSize / 2), y = 9.dp)
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
