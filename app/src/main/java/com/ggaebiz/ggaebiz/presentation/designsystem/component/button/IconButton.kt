package com.ggaebiz.ggaebiz.presentation.designsystem.component.button

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme


@Composable
fun GaeBizIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = GaeBizIconButtonDefaults.shape,
    colors: IconButtonColors = GaeBizIconButtonDefaults.iconButtonColors,
    iconImageVector: ImageVector,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        content = {
            Icon(
                imageVector = iconImageVector,
                contentDescription = null,
            )
        },
    )
}

object GaeBizIconButtonDefaults {
    val shape: RoundedCornerShape
        @Composable
        get() = CircleShape
    val iconButtonColors: IconButtonColors
        @Composable
        get() = IconButtonColors(
            contentColor = GaeBizTheme.colors.black,
            containerColor = GaeBizTheme.colors.gray50,
            disabledContentColor = GaeBizTheme.colors.black,
            disabledContainerColor = GaeBizTheme.colors.gray50,
        )
}

@Preview("Icon Button")
@Composable
private fun GaeBizIconButtonPreview() {
    GaeBizIconButton(
        onClick = {},
        iconImageVector = GaeBizIcon.icBack,
    )
}
