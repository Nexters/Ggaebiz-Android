package com.ggaebiz.ggaebiz.presentation.designsystem.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun FullScreen(
    modifier: Modifier = Modifier,
    statusBarColor: Color = GaeBizTheme.colors.primaryOrange,
    navigationColor: Color = GaeBizTheme.colors.black,
    backGroundImage: Int,
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val defaultColor = GaeBizTheme.colors.gray25

    DisposableEffect(Unit) {
        systemUiController.setStatusBarColor(statusBarColor, darkIcons = true)
        systemUiController.setNavigationBarColor(navigationColor)
        onDispose {
            // TODO :: fix 필요 기존 색상으로 돌아가는 것이 조금 느림 .. ㅜ
            systemUiController.setStatusBarColor(defaultColor, darkIcons = true)
            systemUiController.setNavigationBarColor(defaultColor)
        }
    }

    Box(
        modifier = modifier
            .background(GaeBizTheme.colors.primaryOrange)
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(backGroundImage),
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )
        content()
    }
}

