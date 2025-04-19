package com.ggaebiz.ggaebiz.presentation.ui.onboarding

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
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
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.onboarding.Onboarding.Companion.ONBOARDING_LIST
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = koinViewModel(),
    navigatorHome: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(enabled = true) {
        if (viewModel.uiState.value.backPressedOnce) {
            (context as? Activity)?.finishAffinity()
        } else {
            viewModel.processIntent(OnboardingIntent.ClickBackPressedButton)
        }
    }

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            OnboardingSideEffect.NavigateHome -> navigatorHome()
            OnboardingSideEffect.ToastBackPressed -> showToast(context, R.string.back_provider_toast_text)
        }
    }

    OnboardingContent(
        processIntent = viewModel::processIntent,
        uiState = uiState,
    )
}

@Composable
fun OnboardingContent(
    modifier: Modifier = Modifier,
    processIntent: (OnboardingIntent) -> Unit,
    uiState: OnboardingState,
) {
    val pagerState = rememberPagerState(initialPage = uiState.currentPage, pageCount = { ONBOARDING_LIST.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                processIntent(OnboardingIntent.SwipePager(page))
            }
    }

    BackHandler(enabled = uiState.currentPage > 0) {
        coroutineScope.launch {
            processIntent(OnboardingIntent.ClickBackButton)
            pagerState.animateScrollToPage(uiState.currentPage - 1)
        }
    }
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenHeight = maxHeight

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.6f),
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

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        color = GaeBizTheme.colors.gray900,
                        style = GaeBizTheme.typography.titleBold,
                        text = stringResource(ONBOARDING_LIST[page].descriptionRes),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                ONBOARDING_LIST.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .padding(horizontal = 4.dp)
                            .width(if (index == uiState.currentPage) 10.dp else 6.dp)
                            .background(
                                if (index == uiState.currentPage) {
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
                    .alpha(if (uiState.currentPage < pagerState.pageCount - 1) 1f else 0f),
                onClick = {
                    coroutineScope.launch {
                        processIntent(OnboardingIntent.ClickSkipButton)
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
                        if (uiState.currentPage < pagerState.pageCount - 1) {
                            processIntent(OnboardingIntent.ClickNextButton)
                            pagerState.slowAnimateScrollToPage(uiState.currentPage + 1)
                        } else {
                            processIntent(OnboardingIntent.ClickStartGaebizButton)
                        }
                    }
                },
                contentColor = GaeBizTheme.colors.white,
                containerColor = GaeBizTheme.colors.primaryOrange,
                disabledContentColor = GaeBizTheme.colors.gray400,
                disabledContainerColor = GaeBizTheme.colors.gray100,
                text = if (uiState.currentPage == pagerState.pageCount - 1) {
                    stringResource(R.string.start_ggaebiz_text)
                } else {
                    stringResource(R.string.go_to_next_text)
                },
                style = GaeBizTheme.typography.bodySemiBold,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
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

private fun showToast(context: Context, @StringRes stringResId: Int) {
    Toast.makeText(context, stringResId, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    GaeBizTheme {
        OnboardingScreen(
            navigatorHome = {}
        )
    }
}
