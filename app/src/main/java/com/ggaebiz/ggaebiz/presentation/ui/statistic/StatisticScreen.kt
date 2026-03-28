package com.ggaebiz.ggaebiz.presentation.ui.statistic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel = koinViewModel(),
    navigateBack: () -> Unit,
) {
    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            StatisticSideEffect.NavigateBack -> navigateBack()
        }
    }

    StatisticContent(
        onClickBack = { viewModel.processIntent(StatisticIntent.ClickBack) }
    )
}

@Composable
fun StatisticContent(
    onClickBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        GaeBizTextAppBar(
            titleRes = R.string.statistic_title_text,
            iconOnClick = onClickBack,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticScreenPreview() {
    GaeBizTheme {
        StatisticScreen(
            navigateBack = {}
        )
    }
}