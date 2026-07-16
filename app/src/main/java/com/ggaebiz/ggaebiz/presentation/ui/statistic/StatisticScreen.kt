package com.ggaebiz.ggaebiz.presentation.ui.statistic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.extension.collectAsStateWithLifecycle
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.component.header.GaeBizTextAppBar
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.ui.statistic.component.StatisticCalendarSectionCard
import com.ggaebiz.ggaebiz.presentation.ui.statistic.component.StatisticCharacterRankCard
import com.ggaebiz.ggaebiz.presentation.ui.statistic.component.StatisticTopCard
import com.ggaebiz.ggaebiz.presentation.ui.statistic.component.StatisticTimeCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateSetting: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            StatisticSideEffect.NavigateBack -> navigateBack()
            StatisticSideEffect.NavigateToSetting -> navigateSetting()
        }
    }

    StatisticContent(
        state = uiState,
        onClickBack = { viewModel.processIntent(StatisticIntent.ClickBack) },
        onClickPreviousMonth = { viewModel.processIntent(StatisticIntent.ClickPreviousMonth) },
        onClickNextMonth = { viewModel.processIntent(StatisticIntent.ClickNextMonth) },
        onClickDate = { date -> viewModel.processIntent(StatisticIntent.ClickDate(date)) },
        onSelectFocusPeriod = { period -> viewModel.processIntent(StatisticIntent.ClickFocusPeriod(period)) },
        onSelectRestPeriod = { period -> viewModel.processIntent(StatisticIntent.ClickRestPeriod(period)) },
        onClickStartTimer = { viewModel.processIntent(StatisticIntent.ClickStartTimer) },
    )
}

@Composable
fun StatisticContent(
    state: StatisticState,
    onClickBack: () -> Unit,
    onClickPreviousMonth: () -> Unit,
    onClickNextMonth: () -> Unit,
    onClickDate: (StatisticDateUiModel) -> Unit,
    onSelectFocusPeriod: (StatisticPeriod) -> Unit,
    onSelectRestPeriod: (StatisticPeriod) -> Unit,
    onClickStartTimer: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GaeBizTheme.colors.gray25)
    ) {
        GaeBizTextAppBar(
            titleRes = R.string.statistic_title_text,
            iconOnClick = onClickBack,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                StatisticTopCard(state = state.topCard)
            }
            item {
                StatisticTimeCard(
                    state = state.focusTime,
                    onSelectPeriod = onSelectFocusPeriod,
                )
            }
            item {
                StatisticTimeCard(
                    state = state.restTime,
                    onSelectPeriod = onSelectRestPeriod,
                )
            }
            item {
                StatisticCalendarSectionCard(
                    state = state.calendar,
                    onClickPreviousMonth = onClickPreviousMonth,
                    onClickNextMonth = onClickNextMonth,
                    onClickDate = onClickDate,
                )
            }
            item {
                StatisticCharacterRankCard(
                    state = state.characterRank,
                    onClickStartTimer = onClickStartTimer,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticScreenPreview() {
    GaeBizTheme {
        StatisticContent(
            state = StatisticState(
                focusTime = StatisticTimeCardState(
                    title = "집중 시간",
                    periodLabel = "2025년 3월",
                    breakdown = listOf(
                        StatisticModeBreakdownState(iconRes = null, label = "일반모드", timeText = "0초"),
                        StatisticModeBreakdownState(iconRes = R.drawable.ic_pencil, label = "공부모드", timeText = "0초"),
                        StatisticModeBreakdownState(iconRes = R.drawable.ic_basketball, label = "운동모드", timeText = "0초"),
                    ),
                ),
                restTime = StatisticTimeCardState(
                    title = "휴식 시간",
                    periodLabel = "2025년 3월",
                ),
                calendar = StatisticCalendarState(
                    year = 2025,
                    month = 8,
                    yearMonthText = "8월",
                    dates = (1..31).mapIndexed { index, day ->
                        StatisticDateUiModel(
                            day = day,
                            isCurrentMonth = true,
                            isSelected = false,
                            isToday = day == 1,
                            level = when {
                                index % 5 == 0 -> StatisticLevel.High
                                index % 3 == 0 -> StatisticLevel.Medium
                                index % 2 == 0 -> StatisticLevel.Low
                                else -> StatisticLevel.None
                            }
                        )
                    }
                ),
                characterRank = StatisticCharacterRankState(
                    periodLabel = "2025년 3월",
                    items = listOf(
                        StatisticCharacterRankItemState(1, R.drawable.ic_kiki_level1, "키키", "0 회"),
                        StatisticCharacterRankItemState(2, R.drawable.ic_booboo_level1, "부부", "0 회"),
                        StatisticCharacterRankItemState(3, R.drawable.ic_chacha_level1, "차차", "0 회"),
                        StatisticCharacterRankItemState(4, R.drawable.ic_bobo_level1, "보보", "0 회"),
                        StatisticCharacterRankItemState(5, R.drawable.ic_nana_level1, "나나", "0 회"),
                    ),
                ),
            ),
            onClickBack = {},
            onClickPreviousMonth = {},
            onClickNextMonth = {},
            onClickDate = {},
            onSelectFocusPeriod = {},
            onSelectRestPeriod = {},
            onClickStartTimer = {},
        )
    }
}
