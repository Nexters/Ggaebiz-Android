package com.ggaebiz.ggaebiz.presentation.ui.splash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.FullScreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateHome: () -> Unit,
) {
    val initDuration = 1000
    val animationDuration = 800
    val delayDuration = 800
    var isAnimating by remember { mutableStateOf(false) }

    BackHandler(enabled = true) { }
    
    val animatedHeight by animateDpAsState(
        targetValue = if (isAnimating) 26.dp else 0.dp,
        animationSpec = tween(durationMillis = animationDuration),
        label = "SpacerHeightAnimation"
    )

    LaunchedEffect(Unit) {
        delay(initDuration.toLong())
        isAnimating = true
        delay(animationDuration + delayDuration.toLong())
        navigateHome()
    }

    FullScreen(
        isSplash = true,
        backGroundGradient = GaeBizTheme.colors.gradientOrange,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.weight(1f))
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(1f)
                        .offset(y = 58.dp),
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !isAnimating,
                        exit = fadeOut(animationSpec = tween(animationDuration)),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            Spacer(Modifier.weight(1f))
                            Text(
                                modifier = Modifier.wrapContentWidth(),
                                text = stringResource(R.string.splash_title_text1),
                                style = GaeBizTheme.typography.splash,
                            )
                            Spacer(Modifier.height(59.dp))
                            Text(
                                modifier = Modifier.wrapContentWidth(),
                                text = stringResource(R.string.splash_title_text2),
                                style = GaeBizTheme.typography.splash,
                            )
                        }
                    }

                    Column(modifier = Modifier.wrapContentSize()) {
                        Spacer(Modifier.weight(1f))
                        Image(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(horizontal = 16.dp),
                            painter = painterResource(R.drawable.ggaebiz_eng_white),
                            contentDescription = null,
                            alignment = Alignment.BottomCenter,
                        )
                        Spacer(Modifier.height(animatedHeight))
                        Spacer(Modifier.height(58.dp))
                    }

                    androidx.compose.animation.AnimatedVisibility(
                        visible = isAnimating,
                        enter = fadeIn(animationSpec = tween(animationDuration)),
                    ) {
                        Column(modifier = Modifier.wrapContentSize()) {
                            Spacer(Modifier.weight(1f))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                                    .offset(y = with(LocalDensity.current) { 10.sp.toDp() }),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier.wrapContentSize(),
                                    text = stringResource(R.string.splash_subtitle_text1),
                                    style = GaeBizTheme.typography.bodyMedium,
                                )
                                Text(
                                    modifier = Modifier.wrapContentSize(),
                                    text = stringResource(R.string.splash_subtitle_text2),
                                    style = GaeBizTheme.typography.bodyBold,
                                )
                            }
                            Spacer(Modifier.height(54.dp))
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = animatedHeight),
                    painter = painterResource(R.drawable.ic_ggaebiz),
                    contentDescription = null,
                    alignment = Alignment.BottomCenter,
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    }
}
