package com.ggaebiz.ggaebiz.presentation.ui.statistic

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import java.util.Calendar

@Immutable
data class StatisticState(
    val summaryCard: StatisticSummaryCardState = StatisticSummaryCardState(),
    val focusTime: StatisticTimeCardState = StatisticTimeCardState(title = "집중 시간"),
    val restTime: StatisticTimeCardState = StatisticTimeCardState(title = "휴식 시간"),
    val calendar: StatisticCalendarState = StatisticCalendarState(),
    val characterRank: StatisticCharacterRankState = StatisticCharacterRankState(),
)

@Immutable
data class StatisticSummaryCardState(
    val title: String = "캐릭터 성장 2단계",
    val description: String = "추후 추가 예정인\n테스트용입니다\n빈자리를 비우지 마세요",
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
    data object NavigateToTimer : StatisticSideEffect
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

class StatisticViewModel : BaseViewModel<StatisticState, StatisticIntent, StatisticSideEffect>(
    initialState = run {
        val now = Calendar.getInstance()
        val year = now.get(Calendar.YEAR)
        val month = now.get(Calendar.MONTH) + 1
        val periodLabel = "${year}년 ${month}월"
        StatisticState(
            focusTime = StatisticTimeCardState(
                title = "집중 시간",
                periodLabel = periodLabel,
                breakdown = listOf(
                    StatisticModeBreakdownState(iconRes = null, label = "일반모드", timeText = "0초"),
                    StatisticModeBreakdownState(iconRes = R.drawable.ic_pencil, label = "공부모드", timeText = "0초"),
                    StatisticModeBreakdownState(iconRes = R.drawable.ic_basketball, label = "운동모드", timeText = "0초"),
                ),
            ),
            restTime = StatisticTimeCardState(
                title = "휴식 시간",
                periodLabel = periodLabel,
            ),
            calendar = StatisticCalendarState(
                year = year,
                month = month,
                yearMonthText = "${month}월",
                dates = generateCalendarDates(year, month),
            ),
            characterRank = StatisticCharacterRankState(
                periodLabel = periodLabel,
                items = listOf(
                    StatisticCharacterRankItemState(1, R.drawable.ic_kiki_level1, "키키", "0 회"),
                    StatisticCharacterRankItemState(2, R.drawable.ic_booboo_level1, "부부", "0 회"),
                    StatisticCharacterRankItemState(3, R.drawable.ic_chacha_level1, "차차", "0 회"),
                    StatisticCharacterRankItemState(4, R.drawable.ic_bobo_level1, "보보", "0 회"),
                    StatisticCharacterRankItemState(5, R.drawable.ic_nana_level1, "나나", "0 회"),
                ),
            ),
        )
    }
) {

    fun processIntent(intent: StatisticIntent) {
        when (intent) {
            StatisticIntent.ClickBack -> postSideEffect(StatisticSideEffect.NavigateBack)

            StatisticIntent.ClickPreviousMonth -> shiftMonth(delta = -1)
            StatisticIntent.ClickNextMonth -> shiftMonth(delta = 1)

            is StatisticIntent.ClickDate -> {
                if (!intent.date.isCurrentMonth) return
                updateState { current ->
                    current.copy(calendar = current.calendar.copy(
                        dates = current.calendar.dates.map { item ->
                            item.copy(isSelected = item.isCurrentMonth && item.day == intent.date.day)
                        }
                    ))
                }
            }

            is StatisticIntent.ClickFocusPeriod -> updateState {
                it.copy(focusTime = it.focusTime.copy(selectedPeriod = intent.period))
            }

            is StatisticIntent.ClickRestPeriod -> updateState {
                it.copy(restTime = it.restTime.copy(selectedPeriod = intent.period))
            }

            StatisticIntent.ClickStartTimer -> postSideEffect(StatisticSideEffect.NavigateToTimer)
        }
    }

    private fun shiftMonth(delta: Int) {
        val cal = uiState.value.calendar
        val total = cal.year * 12 + (cal.month - 1) + delta
        val newYear = total / 12
        val newMonth = total % 12 + 1
        val newPeriodLabel = "${newYear}년 ${newMonth}월"
        updateState {
            it.copy(
                calendar = it.calendar.copy(
                    year = newYear,
                    month = newMonth,
                    yearMonthText = "${newMonth}월",
                    dates = generateCalendarDates(newYear, newMonth),
                ),
                focusTime = it.focusTime.copy(periodLabel = newPeriodLabel),
                restTime = it.restTime.copy(periodLabel = newPeriodLabel),
                characterRank = it.characterRank.copy(periodLabel = newPeriodLabel),
            )
        }
    }
}

private fun generateCalendarDates(year: Int, month: Int): List<StatisticDateUiModel> {
    val today = Calendar.getInstance()
    val todayYear = today.get(Calendar.YEAR)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayDay = today.get(Calendar.DAY_OF_MONTH)

    val cal = Calendar.getInstance().apply { set(year, month - 1, 1) }
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val prevCal = Calendar.getInstance().apply { set(year, month - 2, 1) }
    val daysInPrevMonth = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val dates = mutableListOf<StatisticDateUiModel>()

    for (d in (daysInPrevMonth - firstDayOfWeek + 1)..daysInPrevMonth) {
        dates.add(StatisticDateUiModel(day = d, isCurrentMonth = false, isSelected = false, isToday = false))
    }

    for (d in 1..daysInMonth) {
        dates.add(StatisticDateUiModel(
            day = d,
            isCurrentMonth = true,
            isSelected = false,
            isToday = year == todayYear && month == todayMonth && d == todayDay,
        ))
    }

    val remaining = (7 - dates.size % 7) % 7
    for (d in 1..remaining) {
        dates.add(StatisticDateUiModel(day = d, isCurrentMonth = false, isSelected = false, isToday = false))
    }

    return dates
}
