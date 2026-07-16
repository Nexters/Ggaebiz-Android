package com.ggaebiz.ggaebiz.presentation.ui.statistic

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.ggaebiz.ggaebiz.R
import java.util.Calendar

@Immutable
data class StatisticState(
    val topCard: TopCardState = TopCardState(),
    val focusTime: StatisticTimeCardState = StatisticTimeCardState(title = "집중 시간"),
    val restTime: StatisticTimeCardState = StatisticTimeCardState(title = "휴식 시간"),
    val calendar: StatisticCalendarState = StatisticCalendarState(),
    val characterRank: StatisticCharacterRankState = StatisticCharacterRankState(),
)

enum class TopCardCase { LOADING, NEW, STREAK, RETURN, FLOATING_ACTIVE, FLOATING_EMPTY }

@Immutable
data class TopCardState(
    val case: TopCardCase = TopCardCase.LOADING,
    val nickname: String = "",
    @StringRes val subtitleRes: Int? = null,
    val subtitleArg: Int? = null,
    @StringRes val bodyRes: Int? = null,
    val bodyArg: Int? = null,
    @DrawableRes val characterIconRes: Int = R.drawable.ic_positive_kiki,
    val fromName: String? = null,
)

@Immutable
data class StatisticTimeCardState(
    val title: String,
    val periodLabel: String = "",
    val selectedPeriod: StatisticPeriod = StatisticPeriod.Day,
    val hour: String = "00",
    val minute: String = "00",
    val second: String = "00",
    val breakdown: List<StatisticModeBreakdownState> = emptyList(),
)

enum class StatisticPeriod { Month, Week, Day }

@Immutable
data class StatisticModeBreakdownState(
    @DrawableRes val iconRes: Int?,
    val label: String,
    val timeText: String,
)

@Immutable
data class StatisticCalendarState(
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    val month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val yearMonthText: String = "${Calendar.getInstance().get(Calendar.MONTH) + 1}월",
    val dayOfWeeks: List<String> = listOf("일", "월", "화", "수", "목", "금", "토"),
    val dates: List<StatisticDateUiModel> = emptyList(),
)

@Immutable
data class StatisticDateUiModel(
    val day: Int,
    val isCurrentMonth: Boolean,
    val isSelected: Boolean,
    val isToday: Boolean,
    val level: StatisticLevel = StatisticLevel.None,
    val isFeverDay: Boolean = false,
    val isFuture: Boolean = false,
)

enum class StatisticLevel { None, Low, Medium, High }

@Immutable
data class StatisticCharacterRankState(
    val title: String = "캐릭터 사용 빈도",
    val periodLabel: String = "",
    val description: String = "집중과 휴식 타이머 모두 포함된 기록이에요",
    val items: List<StatisticCharacterRankItemState> = emptyList(),
    val ctaCharacterName: String = "캐릭터",
)

@Immutable
data class StatisticCharacterRankItemState(
    val rank: Int,
    @DrawableRes val iconRes: Int,
    val name: String,
    val countText: String,
)

sealed interface StatisticSideEffect {
    data object NavigateBack : StatisticSideEffect
    data object NavigateToSetting : StatisticSideEffect
}

sealed interface StatisticIntent {
    data object ClickBack : StatisticIntent
    data object ClickPreviousMonth : StatisticIntent
    data object ClickNextMonth : StatisticIntent
    data class ClickDate(val date: StatisticDateUiModel) : StatisticIntent
    data class ClickFocusPeriod(val period: StatisticPeriod) : StatisticIntent
    data class ClickRestPeriod(val period: StatisticPeriod) : StatisticIntent
    data object ClickStartTimer : StatisticIntent
}
