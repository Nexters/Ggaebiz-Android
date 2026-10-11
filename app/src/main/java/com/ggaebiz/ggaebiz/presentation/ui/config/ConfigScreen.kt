package com.ggaebiz.ggaebiz.presentation.ui.config

import GaeBizButtonStyle
import GaeBizPopupButton
import GaeBizPopupPosition
import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.component.toast.GaebizToast
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.popup.TextPopup
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

private const val TOAST_DURATION_MILLIS = 3000L

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(ConfigIntent.EnterScreen)
    }
    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            ConfigSideEffect.NavigateBack -> navigateBack()
            ConfigSideEffect.NavigateToLogin -> navigateLogin()
            ConfigSideEffect.MoveToDeviceSetting -> {
                val intent = Intent()
                intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                context.startActivity(intent)
            }
        }
    }

    LaunchedEffect(uiState.toastMessage) {
        if (uiState.toastMessage != null) {
            delay(TOAST_DURATION_MILLIS)
            viewModel.processIntent(ConfigIntent.ClearToast)
        }
    }

    ConfigContent(uiState, viewModel::processIntent)

    if (uiState.isNicknameSheetVisible) {
        NicknameEditDialog(
            nickname = uiState.nicknameInput,
            error = uiState.nicknameError,
            canSave = uiState.canSaveNickname,
            onValueChange = { viewModel.processIntent(ConfigIntent.ChangeNicknameInput(it)) },
            onClickRandom = { viewModel.processIntent(ConfigIntent.ClickRandomNickname) },
            onSave = { viewModel.processIntent(ConfigIntent.ClickSaveNickname) },
            onDismiss = { viewModel.processIntent(ConfigIntent.DismissNicknameSheet) },
        )
    }

    ConfigDialogs(uiState, viewModel::processIntent)
}

@Composable
fun ConfigContent(uiState: ConfigState, processIntent: (ConfigIntent) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            GaeBizTextAppBar(
                titleRes = R.string.config_title_text,
                iconOnClick = { processIntent(ConfigIntent.ClickBack) },
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                ConfigProfileCard(
                    nickname = uiState.nickname,
                    onClickEdit = { processIntent(ConfigIntent.ClickEditNickname) },
                )
                Spacer(Modifier.height(24.dp))
                ConfigSliderSection(
                    iconRes = R.drawable.icon_vibration,
                    mainText = stringResource(R.string.config_vibration_title),
                    isSwitch = true,
                    switchValue = uiState.isVibration,
                    onCheckedChange = { processIntent(ConfigIntent.ChangeSwitch) },
                    isSlider = uiState.isVibrationSlider,
                    sliderValue = uiState.vibrationValue,
                    onSliderChange = { selectedLevel ->
                        processIntent(ConfigIntent.ChangeVibrationValue(selectedLevel))
                    }
                )
                Spacer(Modifier.height(20.dp))
                ConfigSliderSection(
                    iconRes = R.drawable.icon_volume,
                    mainText = stringResource(R.string.config_volume_title),
                    isSwitch = false,
                    sliderValue = uiState.volumeValue,
                    onSliderChange = { selectedLevel ->
                        processIntent(ConfigIntent.ChangeVolumeValue(selectedLevel))
                    }
                )
                Spacer(Modifier.height(20.dp))
                ConfigBatterySection { processIntent(ConfigIntent.ClickCleatButton) }

                Spacer(Modifier.weight(1f))

                ConfigAuthLinks(
                    onClickLogout = { processIntent(ConfigIntent.ClickLogout) },
                    onClickWithdraw = { processIntent(ConfigIntent.ClickWithdraw) },
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        AnimatedVisibility(
            visible = uiState.toastMessage != null,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
        ) {
            uiState.toastMessage?.let { message ->
                GaebizToast(ment = stringResource(message))
            }
        }
    }
}

@Composable
private fun ConfigDialogs(uiState: ConfigState, processIntent: (ConfigIntent) -> Unit) {
    TextPopup(
        visible = uiState.activeDialog == ConfigDialog.Logout,
        onDismissRequest = { processIntent(ConfigIntent.DismissDialog) },
        titleText = stringResource(R.string.logout_dialog_title),
        bodyText = stringResource(R.string.logout_dialog_body),
        position = GaeBizPopupPosition.Center,
        buttons = listOf(
            GaeBizPopupButton(
                text = stringResource(R.string.dialog_cancel),
                style = GaeBizButtonStyle.Secondary,
                onClick = { processIntent(ConfigIntent.DismissDialog) },
            ),
            GaeBizPopupButton(
                text = stringResource(R.string.dialog_confirm),
                style = GaeBizButtonStyle.Primary,
                onClick = { processIntent(ConfigIntent.ConfirmLogout) },
            ),
        ),
    )

    TextPopup(
        visible = uiState.activeDialog == ConfigDialog.Withdraw,
        onDismissRequest = { processIntent(ConfigIntent.DismissDialog) },
        titleText = stringResource(R.string.withdraw_dialog_title),
        bodyText = stringResource(R.string.withdraw_dialog_body),
        position = GaeBizPopupPosition.Center,
        buttons = listOf(
            GaeBizPopupButton(
                text = stringResource(R.string.dialog_cancel),
                style = GaeBizButtonStyle.Secondary,
                onClick = { processIntent(ConfigIntent.DismissDialog) },
            ),
            GaeBizPopupButton(
                text = stringResource(R.string.dialog_confirm),
                style = GaeBizButtonStyle.Danger,
                onClick = { processIntent(ConfigIntent.ConfirmWithdraw) },
            ),
        ),
    )
}


@Preview(showBackground = true)
@Composable
fun ConfigScreenPreview() {
    GaeBizTheme {
        ConfigScreen(
            navigateBack = {},
            navigateLogin = {},
        )
    }
}
