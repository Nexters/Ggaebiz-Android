package com.ggaebiz.ggaebiz.presentation.designsystem.component

import androidx.compose.foundation.shape.CircleShape
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
    shape: Shape? = null,
    colors: IconButtonColors? = null,
    icon: @Composable () -> Unit,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape ?: GgaebizIconButtonDefaults.shape,
        colors = colors ?: GgaebizIconButtonDefaults.iconButtonColors,
    ) {
        icon()
    }
}

object GgaebizIconButtonDefaults {
    val shape = CircleShape
    val iconButtonColors = IconButtonColors(
        contentColor = Black,
        containerColor = Gray50,
        disabledContentColor = Black,
        disabledContainerColor = Gray50,
    )
}
