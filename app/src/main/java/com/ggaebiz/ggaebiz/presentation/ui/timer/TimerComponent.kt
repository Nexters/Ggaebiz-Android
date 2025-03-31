package com.ggaebiz.ggaebiz.presentation.ui.timer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.ggaebiz.ggaebiz.presentation.designsystem.component.timer.GaeBizTimer
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizMent


@Stable
@Composable
fun CenterComponent(
    textRes : Int,
    composition : LottieComposition?,
    progress : () -> Float,
    seconds : Int,
    screenWidth : Dp
){
    val imageWidth = screenWidth / 3 * 2
    GaeBizMent(
        text = stringResource(textRes),
    )
    Spacer(modifier = Modifier.height(11.dp))
    LottieAnimation(
        composition = composition,
        progress =  progress ,
        modifier = Modifier.size(imageWidth),
    )
    GaeBizTimer(remainingSeconds = seconds)
}
