package com.ggaebiz.ggaebiz.presentation.ui.setting

import GaeBizPopupPosition
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.CategoryArea
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizTimePicker
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.LevelItem
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.RestMent
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.SettingSwitch
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.popup.ListPopup
import com.ggaebiz.ggaebiz.presentation.model.Character.Companion.CHARACTER_LIST
import com.ggaebiz.ggaebiz.presentation.model.Character.Companion.SETTING_MENT_LIST
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = koinViewModel(),
    navigatorHome: () -> Unit,
    navigateTimer: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            is SettingSideEffect.NavigateToTimer -> navigateTimer()
        }
    }

    BackHandler(enabled = uiState.isLevelPopupVisible) {
        viewModel.processIntent(SettingIntent.ClickMentLevel(false))
    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(SettingIntent.EnterScreen)
    }

    SettingContent(
        uiState = uiState,
        processIntent = viewModel::processIntent,
        onClickBackButton = { navigatorHome() },
    )
}

@Composable
fun SettingContent(
    uiState: SettingState,
    processIntent: (SettingIntent) -> Unit,
    onClickBackButton: () -> Unit,
) {
    var buttonEnabled by remember {
        mutableStateOf(uiState.selectedHour != "00" && uiState.selectedMinute != "00")
    }
    val spacerHeightPx = remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(uiState.selectedHour, uiState.selectedMinute) {
        buttonEnabled = !(uiState.selectedHour == "00" && uiState.selectedMinute == "00")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = !uiState.isNudgeGuideViewed
            ) {
                processIntent(SettingIntent.CloseNudgePopUp)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GaeBizTextAppBar(
            titleRes = R.string.setting_title_text,
            iconOnClick = { onClickBackButton() },
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingSwitch(
            timerMode = uiState.timerMode,
            onToggle = { isRestSelected ->
                processIntent(SettingIntent.ClickTimerMode(isRestSelected))
            },
        )
        Spacer(modifier = Modifier.height(36.dp))
        Image(
            painter = painterResource(
                selectedCharacterImageRes(uiState)
            ),
            contentDescription = null,
            modifier = Modifier.size(125.dp),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (val mode = uiState.timerMode) {
            is TimerMode.Rest -> {
                RestMent(
                    text = stringResource(SETTING_MENT_LIST[uiState.level - 1]),
                    level = uiState.level,
                    onClick = {
                        processIntent(SettingIntent.ClickMentLevel(true))
                    },
                )
            }
            is TimerMode.Concentrate -> {
                Spacer(modifier = Modifier.height(12.dp))
                CategoryArea(
                    selected = mode,
                    onSelect = { type ->
                        processIntent(SettingIntent.ClickTimerMode(TimerMode.Concentrate(type)))
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier
                .weight(1f)
                .onGloballyPositioned {
                    spacerHeightPx.floatValue = it.size.height.toFloat()
                }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            GaeBizTimePicker(
                selectedHour = uiState.selectedHour,
                selectedMinute = uiState.selectedMinute,
                maxHour = uiState.maxHour,
                maxMinute = uiState.maxMinute,
                timerMode = uiState.timerMode,
                onHourSelected = { processIntent(SettingIntent.SelectHour(it)) },
                onMinuteSelected = { processIntent(SettingIntent.SelectMinute(it)) },
            )
            this@Column.AnimatedVisibility(
                visible = !uiState.isNudgeGuideViewed,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                SettingNudgePopup(
                    density,
                    spacerHeightPx.floatValue,
                ) { processIntent(SettingIntent.CloseNudgePopUp) }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        GaeBizButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .padding(horizontal = 20.dp),
            enabled = buttonEnabled,
            onClick = {
                processIntent(
                    SettingIntent.ClickStartButton(
                        hour = uiState.selectedHour.toInt(),
                        minute = uiState.selectedMinute.toInt(),
                    ),
                )
            },
            contentColor = GaeBizTheme.colors.white,
            containerColor = GaeBizTheme.colors.gray800,
            disabledContentColor = GaeBizTheme.colors.gray400,
            disabledContainerColor = GaeBizTheme.colors.gray100,
            text = if (uiState.timerMode is TimerMode.Rest) {
                stringResource(R.string.start_rest_button_text, stringResource(CHARACTER_LIST[uiState.selectedCharacterIdx].nameResId))
            } else {
                stringResource(R.string.start_concentrate_button_text, stringResource(CHARACTER_LIST[uiState.selectedCharacterIdx].nameResId))
            },
            style = GaeBizTheme.typography.bodySemiBold,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
    ChoiceLevelPopup(
        uiState.isLevelPopupVisible,
        uiState.selectedCharacterIdx,
        uiState.level,
    ) { selectedLevel ->
        processIntent(SettingIntent.SelectLevel(selectedLevel))
        processIntent(SettingIntent.ClickMentLevel(false))
    }
}

@Composable
fun ChoiceLevelPopup(
    visible: Boolean,
    selectedCharacterIdx: Int,
    selectedLevel: Int,
    onClick: (Int) -> Unit,
){
    ListPopup(
        visible = visible,
        titleText = stringResource(R.string.ment_level_title_text),
        subtitleText = stringResource(R.string.ment_level_subtitle_text),
        position = GaeBizPopupPosition.Bottom,
        itemContent = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LevelItem(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.ment_level_1_item_text),
                    isSelected = selectedLevel == 1,
                    selectedIcon = painterResource(CHARACTER_LIST[selectedCharacterIdx].selectedImageResId[0]),
                    unSelectedIcon = painterResource(CHARACTER_LIST[selectedCharacterIdx].unSelectedImageResId[0]),
                    onClick = { onClick(1) },
                )
                LevelItem(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.ment_level_2_item_text),
                    isSelected = selectedLevel == 2,
                    selectedIcon = painterResource(CHARACTER_LIST[selectedCharacterIdx].selectedImageResId[1]),
                    unSelectedIcon = painterResource(CHARACTER_LIST[selectedCharacterIdx].unSelectedImageResId[1]),
                    onClick = { onClick(2) },
                )
                LevelItem(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.ment_level_3_item_text),
                    isSelected = selectedLevel == 3,
                    selectedIcon = painterResource(CHARACTER_LIST[selectedCharacterIdx].selectedImageResId[2]),
                    unSelectedIcon = painterResource(CHARACTER_LIST[selectedCharacterIdx].unSelectedImageResId[2]),
                    onClick = { onClick(3) },
                )
            }
        }
    )
}

private fun selectedCharacterImageRes(uiState: SettingState): Int {
    return when {
        uiState.timerMode.isRestTimer() -> {
            CHARACTER_LIST[uiState.selectedCharacterIdx].selectedImageResId[uiState.level - 1]
        }
        uiState.timerMode.isConcentrateTimer() -> {
            when {
                (uiState.timerMode as TimerMode.Concentrate).isNormal() -> {
                    CHARACTER_LIST[uiState.selectedCharacterIdx].selectedImageResId[uiState.level - 1]
                }
                (uiState.timerMode as TimerMode.Concentrate).isStudy() -> {
                    CHARACTER_LIST[uiState.selectedCharacterIdx].selectedConcentrateStudyImageResId
                }
                (uiState.timerMode as TimerMode.Concentrate).isExercise() -> {
                    CHARACTER_LIST[uiState.selectedCharacterIdx].selectedConcentrateExerciseImageResId
                }

                else -> CHARACTER_LIST[uiState.selectedCharacterIdx].selectedImageResId[uiState.level - 1]
            }
        }

        else -> CHARACTER_LIST[uiState.selectedCharacterIdx].selectedImageResId[uiState.level - 1]
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    GaeBizTheme {
        SettingScreen(
            navigateTimer = {},
            navigatorHome = {}
        )
    }
}
