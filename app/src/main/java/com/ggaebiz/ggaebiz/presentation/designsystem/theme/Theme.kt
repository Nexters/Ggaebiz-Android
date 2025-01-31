package com.ggaebiz.ggaebiz.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun GaeBizTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider (
        LocalGaeBizColor provides GaeBizColorScheme,
        LocalGaeBizTypography provides Typography

    ){
        MaterialTheme(
            colorScheme = lightColorScheme(background = GaeBizTheme.colors.gray25),
            content = content
        )
    }
}

object GaeBizTheme{
    val colors : GaeBizColors
        @Composable
        get() = LocalGaeBizColor.current

    val typography : GaeBizTypography
        @Composable
        get() = LocalGaeBizTypography.current
}