package com.ggaebiz.ggaebiz.presentation.designsystem.component.switch

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme


@Composable
fun SwitchBasic(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    colors: SwitchColors = SwitchDefaults.colors,
    trackWidth: Dp,
    trackHeight: Dp,
    cornerRadius: Dp,
    thumbSize: Dp,
    thumbPadding: Dp,
) {
    val trackColor = colors.trackColor(checked)
    val thumbColor = colors.thumbColor(checked)

    val offsetAnimation by animateDpAsState(
        targetValue = if (checked) {
            trackWidth - thumbSize - thumbPadding * 2
        } else {
            0.dp
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "offsetAnimation",
    )

    Box {
        Box(
            modifier = modifier
                .width(trackWidth)
                .height(trackHeight)
                .clip(RoundedCornerShape(cornerRadius))
                .background(trackColor)
                .clickable { onCheckedChange(!checked) },
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .padding(thumbPadding)
                    .offset {
                        IntOffset(offsetAnimation.roundToPx(), 0)
                    }
                    .size(thumbSize)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(thumbColor)
            )
        }

        if (enabled.not()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(colors.disabledMaskColor)
            )
        }
    }
}

@Composable
fun GaeBizSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    colors: SwitchColors = SwitchDefaults.colors,
    trackWidth: Dp = SwitchDefaults.trackWidthMedium,
    trackHeight: Dp = SwitchDefaults.trackHeightMedium,
    cornerRadius: Dp = SwitchDefaults.cornerRadiusMedium,
    thumbSize: Dp = SwitchDefaults.thumbSizeMedium,
    thumbPadding: Dp = SwitchDefaults.thumbPaddingMedium,
) {
    SwitchBasic(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        modifier = modifier,
        colors = colors,
        trackWidth = trackWidth,
        trackHeight = trackHeight,
        cornerRadius = cornerRadius,
        thumbSize = thumbSize,
        thumbPadding = thumbPadding,
    )
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
private fun SwitchPreview() {
    GaeBizTheme {
        var checked by remember { mutableStateOf(false) }
        GaeBizSwitch(
            checked = checked,
            enabled = true,
            onCheckedChange = { checked = it },
        )
    }
}
