package com.ggaebiz.ggaebiz.presentation.ui.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizLogoRightIconAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.component.toast.GaebizToast
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizMent
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizTag
import com.ggaebiz.ggaebiz.presentation.model.Character
import com.ggaebiz.ggaebiz.presentation.model.Character.Companion.CHARACTER_LIST
import com.ggaebiz.ggaebiz.presentation.service.TimerServiceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateSetting: () -> Unit,
    navigateAlarm: () -> Unit,
    navigateConfig: () -> Unit
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

    var showToast by remember { mutableStateOf(false) }
    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            is HomeSideEffect.NoticeVolumeOff -> showToast = true
            is HomeSideEffect.NavigateToSetting -> navigateSetting()
            is HomeSideEffect.CheckPermission -> {
                if (!checkPermission) requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            HomeSideEffect.NavigateToConfig -> navigateConfig()
        }
    }

    HomeContent(
        showToast = showToast,
        onDismissToast = { showToast = false },
        processIntent = viewModel::processIntent,
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    showToast: Boolean,
    onDismissToast: () -> Unit,
    processIntent: (HomeIntent) -> Unit,
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GaeBizLogoRightIconAppBar (clickRightIcon = {processIntent(HomeIntent.ClickConfigButton)})
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
                val isActive = page == pagerState.currentPage

                AnimatedCharacterItem(
                    character = CHARACTER_LIST[page],
                    imageWidth = imageWidth,
                    exoPlayer = exoPlayer,
                    isActive = isActive,
                    playMent = {
                        processIntent(HomeIntent.PlayMentAudio)
                    },
                )
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

        LaunchedEffect(showToast) {
            delay(2000)
            onDismissToast()
        }

        AnimatedVisibility(
            visible = showToast,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)),
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 116.dp),
        ) {
            GaebizToast(
                ment = context.getString(
                    CHARACTER_LIST[pagerState.currentPage].nameResId
                ) + "가 말 하고 있어요. 볼륨을 켜주세요."
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}

@Composable
fun AnimatedCharacterItem(
    character: Character,
    imageWidth: Dp,
    exoPlayer: ExoPlayer,
    isActive: Boolean,
    playMent: () -> Unit,
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val scale by animateFloatAsState(
        targetValue = if (isPressed || isAnimating) 0.9f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow), label = ""
    )

    val soundUri = Uri.parse("android.resource://${context.packageName}/${character.initMentAudioResId}")

    LaunchedEffect(isActive) {
        if (!isActive) {
            exoPlayer.pause()
            isPlaying = false
        }
    }
    
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    isPlaying = false
                }
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    fun handleTap() {
        if (isPlaying) {
            exoPlayer.pause()
            isPlaying = false
        } else {
            playMent.invoke()
            exoPlayer.setMediaItem(MediaItem.fromUri(soundUri))
            exoPlayer.prepare()
            exoPlayer.seekTo(0)
            exoPlayer.play()
            isPlaying = true
        }
    }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        scope.launch {
                            isAnimating = true
                            delay(250)
                            isAnimating = false
                        }
                        handleTap()
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = character.imageResId[0]),
            contentDescription = null,
            modifier = Modifier.size(imageWidth),
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    GaeBizTheme {
        HomeScreen(
            navigateAlarm = {},
            navigateSetting = {},
            navigateConfig = {}
        )
    }
}
