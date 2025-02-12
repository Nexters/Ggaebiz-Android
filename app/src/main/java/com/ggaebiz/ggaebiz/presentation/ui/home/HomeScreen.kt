package com.ggaebiz.ggaebiz.presentation.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizLogoAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizMent
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizTag
import com.ggaebiz.ggaebiz.presentation.model.Character.Companion.CHARACTER_LIST
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateSetting: () -> Unit,
    navigateAlarm: () -> Unit,
) {
    val timerServiceManager: TimerServiceManager by getKoin().inject()
    if (timerServiceManager.isTimerServiceRunning(LocalContext.current)) {
        navigateAlarm()
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> viewModel.processIntent(HomeIntent.UpdatePermission(isGranted)) }
    )

    val checkPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            is HomeSideEffect.NavigateToSetting -> navigateSetting()
            is HomeSideEffect.CheckPermission -> {
                if (!checkPermission) requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    HomeContent(processIntent = viewModel::processIntent)
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    processIntent: (HomeIntent) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { CHARACTER_LIST.size })
    val selectedCharacter = CHARACTER_LIST[pagerState.currentPage]

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageWidth = screenWidth / 3 * 2
    val sideOffset = screenWidth / 6

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            processIntent(HomeIntent.SelectCharacter(page))
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GaeBizLogoAppBar()

        Spacer(modifier = Modifier.height(58.dp))
        GaeBizMent(
            text = stringResource(selectedCharacter.initMentResId),
        )

        Spacer(modifier = Modifier.height(30.dp))
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = sideOffset),
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(CHARACTER_LIST[page].imageResId[0]),
                    contentDescription = null,
                    modifier = Modifier.size(imageWidth),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(selectedCharacter.wholeNameResId),
            style = GaeBizTheme.typography.titleSemiBold,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            selectedCharacter.traitsResIdList.forEach { trait ->
                GaeBizTag(text = stringResource(trait))
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            CHARACTER_LIST.indices.forEach { index ->
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
                .padding(start = 20.dp, end = 20.dp),
            onClick = { processIntent(HomeIntent.ClickSettingButton) },
            contentColor = GaeBizTheme.colors.white,
            containerColor = GaeBizTheme.colors.gray800,
            disabledContentColor = GaeBizTheme.colors.gray400,
            disabledContainerColor = GaeBizTheme.colors.gray100,
            text = stringResource(
                R.string.setting_button_text,
                stringResource(selectedCharacter.nameResId)
            ),
            style = GaeBizTheme.typography.bodySemiBold,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    GaeBizTheme {
        HomeScreen(
            navigateAlarm = {},
            navigateSetting = {},
    }
}
