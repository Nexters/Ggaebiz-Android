package com.ggaebiz.ggaebiz.presentation.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.button.GaeBizButton
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.component.picker.PickerState
import com.ggaebiz.ggaebiz.presentation.designsystem.component.picker.rememberPickerState
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizLevelSlider
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizMent
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.GaeBizTimePicker
import com.ggaebiz.ggaebiz.presentation.feature.Character.Companion.CHARACTER_LIST
import com.ggaebiz.ggaebiz.presentation.feature.Character.Companion.SETTING_MENT_LIST
import com.ggaebiz.ggaebiz.presentation.ui.naviagation.NavigatorState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = koinViewModel(),
    navigator: NavigatorState,
    navigateTimer: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            is SettingSideEffect.NavigateToTimer -> navigateTimer()
        }
    }

    SettingContent(
        uiState = uiState,
        processIntent = viewModel::processIntent,
        onClickBackButton = navigator::popBackStack,
    )
}

@Composable
fun SettingContent(
    uiState: SettingState,
    processIntent: (SettingIntent) -> Unit,
    onClickBackButton: () -> Unit,
) {
    val hourPickerState: PickerState = rememberPickerState(
        defaultValue = uiState.hour.toString().padStart(2, '0')
    )
    val minutePickerState: PickerState = rememberPickerState(
        defaultValue = uiState.minute.toString().padStart(2, '0')
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GaeBizTextAppBar(
            titleRes = R.string.setting_title_text,
            iconOnClick = { onClickBackButton() },
        )

        Spacer(modifier = Modifier.height(21.dp))
        Image(
            painter = painterResource(
                CHARACTER_LIST[uiState.selectedCharacterIdx].selectedImageResId[uiState.level - 1]
            ),
            contentDescription = null,
            modifier = Modifier.size(125.dp),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(24.dp))
        GaeBizMent(
            text = stringResource(SETTING_MENT_LIST[uiState.level - 1]),
        )

        Spacer(modifier = Modifier.height(24.dp))
        GaeBizLevelSlider(
            selectedLevel = uiState.level,
            onValueChange = { selectedLevel ->
                processIntent(SettingIntent.SelectLevel(selectedLevel))
            },
        )

        Spacer(modifier = Modifier.height(66.dp))
        GaeBizTimePicker(
            hourPickerState = hourPickerState,
            minutePickerState = minutePickerState,
            onScrollFinished = {
                processIntent(SettingIntent.SelectTime(
                    hour = hourPickerState.selectedItem.toInt(),
                    minute = minutePickerState.selectedItem.toInt()),
                )
            },
        )

        Spacer(modifier = Modifier.weight(1f))
        GaeBizButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            enabled = uiState.buttonEnabled,
            onClick = { processIntent(SettingIntent.ClickStartButton) },
            contentColor = GaeBizTheme.colors.white,
            containerColor = GaeBizTheme.colors.gray800,
            disabledContentColor = GaeBizTheme.colors.gray400,
            disabledContainerColor = GaeBizTheme.colors.gray100,
            text = stringResource(R.string.start_button_text),
            style = GaeBizTheme.typography.bodySemiBold,
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
