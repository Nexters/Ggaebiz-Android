package com.ggaebiz.ggaebiz.presentation.designsystem.component.timer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.presentation.designsystem.component.icon.GaeBizIcon
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme


@Composable
private fun TimerColon(
    modifier: Modifier = Modifier,
    isBlack: Boolean = true,
) {
    val tineColor = if (isBlack) GaeBizTheme.colors.black else GaeBizTheme.colors.white
    Box(
        modifier = modifier
    ) {
        Icon(
            modifier = Modifier.align(Alignment.TopCenter),
            imageVector = GaeBizIcon.icColon,
            tint = tineColor,
            contentDescription = ""
        )
    }
}

@Composable
fun TimerLargeColon(
    modifier: Modifier = Modifier,
    isBlack: Boolean = true,

    ) {
    TimerColon(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .width(13.dp)
            .height(52.dp),
        isBlack = isBlack
    )
}

@Composable
fun TimerSmallColon(
    modifier: Modifier = Modifier,
    isBlack: Boolean = true,
) {
    TimerColon(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .width(8.dp)
            .height(35.dp),
        isBlack = isBlack
    )
}