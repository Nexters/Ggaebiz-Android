package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme

@Composable
fun FullScreen(
    modifier: Modifier = Modifier,
    backGroundImage: Int? = null,
    backGroundGradient: List<Color>? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .background(GaeBizTheme.colors.primaryOrange)
            .fillMaxSize(),
    ) {
        if (backGroundImage != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(backGroundImage),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
            )
        }
        if (backGroundGradient != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = backGroundGradient,
                        ),
                    ),
            )
        }
        content()
    }
}
