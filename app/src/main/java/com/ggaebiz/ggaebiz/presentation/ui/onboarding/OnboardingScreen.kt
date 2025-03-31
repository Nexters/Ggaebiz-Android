package com.ggaebiz.ggaebiz.presentation.ui.onboarding

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.onboarding.Onboarding.Companion.ONBOARDING_LIST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = koinViewModel(),
    navigatorHome: () -> Unit,
) {
    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler(enabled = true) {
        if (backPressedOnce) {
            (context as? Activity)?.finishAffinity()
        } else {
            backPressedOnce = true
            Toast.makeText(context, R.string.back_provider_toast_text, Toast.LENGTH_SHORT).show()

            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                backPressedOnce = false
            }
        }
    }

    OnboardingContent(
        processIntent = viewModel::processIntent,
        onClickStartButton = { navigatorHome() },
    )
}

@Composable
fun OnboardingContent(
    modifier: Modifier = Modifier,
    processIntent: (OnboardingIntent) -> Unit,
    onClickStartButton: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { ONBOARDING_LIST.size })
    val coroutineScope = rememberCoroutineScope()

    val currentPage = pagerState.currentPage

    BackHandler(enabled = currentPage > 0) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(currentPage - 1)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = ONBOARDING_LIST[page].onBoardingImgRes),
                    contentDescription = stringResource(R.string.logo_img_description),
                )

                Spacer(modifier = Modifier.height(47.5.dp))
                Text(
                    color = GaeBizTheme.colors.gray900,
                    style = GaeBizTheme.typography.titleBold,
                    text = stringResource(ONBOARDING_LIST[page].descriptionRes),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(41.5.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            ONBOARDING_LIST.indices.forEach { index ->
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .padding(horizontal = 4.dp)
                        .width(if (index == pagerState.currentPage) 10.dp else 6.dp)
                        .background(
                            if (index == pagerState.currentPage) {
                                GaeBizTheme.colors.primaryOrange
                            } else {
                                GaeBizTheme.colors.gray200
                            },
                            shape = RoundedCornerShape(50),
                        ),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        GaeBizButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .alpha(if (currentPage < pagerState.pageCount - 1) 1f else 0f),
            onClick = {
                coroutineScope.launch {
                    pagerState.slowAnimateScrollToPage(pagerState.pageCount - 1)
                }
            },
            contentColor = GaeBizTheme.colors.gray600,
            containerColor = Color.Transparent,
            disabledContentColor = GaeBizTheme.colors.white,
            disabledContainerColor = GaeBizTheme.colors.white,
            text = stringResource(R.string.skip_text),
            style = GaeBizTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(12.dp))
        GaeBizButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            onClick = {
                coroutineScope.launch {
                    if (currentPage < pagerState.pageCount - 1) {
                        pagerState.slowAnimateScrollToPage(currentPage + 1)
                    } else {
                        processIntent(OnboardingIntent.ClickStartGaebizButton)
                        onClickStartButton()
                    }
                }
            },
            contentColor = GaeBizTheme.colors.white,
            containerColor = GaeBizTheme.colors.primaryOrange,
            disabledContentColor = GaeBizTheme.colors.gray400,
            disabledContainerColor = GaeBizTheme.colors.gray100,
            text = if (currentPage == pagerState.pageCount - 1) {
                stringResource(R.string.start_ggaebiz_text)
            } else {
                stringResource(R.string.go_to_next_text)
            },
            style = GaeBizTheme.typography.bodySemiBold,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

suspend fun PagerState.slowAnimateScrollToPage(
    targetPage: Int,
    animationDurationMillis: Int = 600,
) {
    animateScrollToPage(
        page = targetPage,
        animationSpec = tween(
            durationMillis = animationDurationMillis,
            easing = FastOutSlowInEasing,
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    GaeBizTheme {
        OnboardingContent(
            processIntent = {},
            onClickStartButton = {}
        )
    }
}
