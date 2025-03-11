package com.ggaebiz.ggaebiz.presentation.ui.config

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
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

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when(effect){
            ConfigSideEffect.NavigateBack -> navigateBack()
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