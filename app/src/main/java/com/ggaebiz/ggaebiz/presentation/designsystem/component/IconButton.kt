package com.ggaebiz.ggaebiz.presentation.designsystem.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.Black
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.Gray50

@Composable
fun GgaebizIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = GgaebizIconButtonDefaults.shape,
    colors: IconButtonColors = GgaebizIconButtonDefaults.iconButtonColors,
    icon: @Composable () -> Unit,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
    ) {
        icon()
    }
}

object GgaebizIconButtonDefaults {
    val shape: RoundedCornerShape
        @Composable
        get() = CircleShape
    val iconButtonColors: IconButtonColors
        @Composable
        get() = IconButtonColors(
            contentColor = Black,
            containerColor = Gray50,
            disabledContentColor = Black,
            disabledContainerColor = Gray50,
        )
}
