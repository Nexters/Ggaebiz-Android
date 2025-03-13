package com.ggaebiz.ggaebiz.presentation.ui.config

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = koinViewModel(),
    navigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(ConfigIntent.EnterScreen)
    }
    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            ConfigSideEffect.NavigateBack -> navigateBack()
            ConfigSideEffect.MoveToDeviceSetting -> {
                val intent = Intent()
                intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                context.startActivity(intent)
            }
        }
    }

    ConfigContent(uiState, viewModel::processIntent)
}

@Composable
fun ConfigContent(uiState: ConfigState, processIntent: (ConfigIntent) -> Unit) {
    Column {
        GaeBizTextAppBar(
            titleRes = R.string.config_title_text,
            iconOnClick = { processIntent(ConfigIntent.ClickBack) },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ConfigScreenPreview() {
    GaeBizTheme {
        ConfigScreen(
            navigateBack = {}
        )
    }
}