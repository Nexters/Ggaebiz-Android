package com.ggaebiz.ggaebiz.presentation.designsystem.component.switch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Immutable
object SwitchDefaults {
    val colors
        @Composable
        get() = SwitchColors(
            checkedTrackColor = GaeBizTheme.colors.gray900,
            uncheckedTrackColor = GaeBizTheme.colors.gray75,

            checkedThumbColor = GaeBizTheme.colors.white,
            uncheckedThumbColor = GaeBizTheme.colors.white,

            disabledMaskColor = GaeBizTheme.colors.white.copy(alpha = 0.43f)
        )

    val trackWidthMedium = 52.dp

    val trackHeightMedium = 32.dp

    val cornerRadiusMedium = 100.dp

    val thumbSizeMedium = 24.dp

    val thumbPaddingMedium = 4.dp
}

@Immutable
data class SwitchColors(
    val checkedTrackColor: Color,
    val uncheckedTrackColor: Color,

    val checkedThumbColor: Color,
    val uncheckedThumbColor: Color,

    val disabledMaskColor: Color,
) {
    @Stable
    internal fun trackColor(
        checked: Boolean,
    ): Color = when {
        checked -> checkedTrackColor
        else -> uncheckedTrackColor
    }

    @Stable
    internal fun thumbColor(
        checked: Boolean,
    ): Color = when {
        checked -> checkedThumbColor
        else -> uncheckedThumbColor
    }
}