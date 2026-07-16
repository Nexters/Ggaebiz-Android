package com.ggaebiz.ggaebiz.presentation.ui.statistic

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.ggaebiz.ggaebiz.domain.model.CalendarMonth
import com.ggaebiz.ggaebiz.domain.model.TimerTimeRecord
import com.ggaebiz.ggaebiz.domain.model.TopCardData
import com.ggaebiz.ggaebiz.domain.repository.NicknameRepository
import com.ggaebiz.ggaebiz.domain.usecase.GetCalendarUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetTimerTimesUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetTopCardDataUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

class StatisticViewModel(
    private val getTopCardDataUseCase: GetTopCardDataUseCase,
    private val nicknameRepository: NicknameRepository,
    private val getTimerTimesUseCase: GetTimerTimesUseCase,
    private val getCalendarUseCase: GetCalendarUseCase,
) : BaseViewModel<StatisticState, StatisticIntent, StatisticSideEffect>(
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

    private var timerTimes: List<TimerTimeRecord> = emptyList()
    private var calendarMonths: Map<String, CalendarMonth> = emptyMap()

    init {
        loadTopCard()
        loadTimerTimes()
        loadCalendar(yearMonthKey(uiState.value.calendar.year, uiState.value.calendar.month))
    }

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
                it.copy(focusTime = buildFocusCard(it.focusTime.copy(selectedPeriod = intent.period)))
            }

            is StatisticIntent.ClickRestPeriod -> updateState {
                it.copy(restTime = buildRestCard(it.restTime.copy(selectedPeriod = intent.period)))
            }

            StatisticIntent.ClickStartTimer -> postSideEffect(StatisticSideEffect.NavigateToTimer)
        }
    }

    private fun loadTopCard() = launch {
        val nickname = nicknameRepository.getNickname().orEmpty()
        getTopCardDataUseCase()
            .onSuccess { data -> updateState { it.copy(topCard = buildTopCard(data, nickname)) } }
            .onFailure { updateState { it.copy(topCard = fallbackTopCard(nickname)) } }
    }

    /** 판정 트리: 신규 > 연속(streakDays≥2) > 복귀(2≤O<7) > 그외(빈도 있음/없음). */
    private fun buildTopCard(data: TopCardData, nickname: String): TopCardState {
        val counts = data.selectionCountList
        val hasFrequency = counts.sum() > 0
        val favoriteIdx = if (hasFrequency) counts.indices.maxByOrNull { counts[it] } ?: -1 else -1
        val favorite = favoriteIdx.takeIf { it in CharacterName.entries.indices }
            ?.let { CharacterName.entries[it] }
        val restDays = daysSince(data.lastAttendanceDate)

        return when {
            // 1. 신규 — 기록이 아예 없음
            data.streakDays == 0 && data.lastAttendanceDate == null -> TopCardState(
                case = TopCardCase.NEW,
                nickname = nickname,
                subtitleRes = R.string.statistic_top_card_new_subtitle,
                bodyRes = R.string.statistic_top_card_new_body,
                characterIconRes = CharacterName.KIKI.iconRes(),
                fromName = CharacterName.KIKI.koreanName(),
            )

            // 2. 연속 — 이번 달 2일 이상 연속
            data.streakDays >= STREAK_THRESHOLD -> {
                val character = favorite ?: CharacterName.KIKI
                TopCardState(
                    case = TopCardCase.STREAK,
                    nickname = nickname,
                    bodyRes = R.string.statistic_top_card_streak_body,
                    bodyArg = data.streakDays,
                    characterIconRes = character.iconRes(),
                    fromName = character.koreanName(),
                )
            }

            // 3. 복귀 — 2일 이상 쉬고 일주일 미만
            restDays != null && restDays in RETURN_MIN_DAYS until RETURN_MAX_DAYS -> {
                val character = favorite ?: CharacterName.KIKI
                TopCardState(
                    case = TopCardCase.RETURN,
                    nickname = nickname,
                    subtitleRes = R.string.statistic_top_card_return_subtitle,
                    subtitleArg = restDays,
                    bodyRes = R.string.statistic_top_card_return_body,
                    bodyArg = restDays,
                    characterIconRes = character.iconRes(),
                    fromName = character.koreanName(),
                )
            }

            // 4-A. 그 외 — 캐릭터 빈도 데이터 있음
            hasFrequency && favorite != null -> TopCardState(
                case = TopCardCase.FLOATING_ACTIVE,
                nickname = nickname,
                subtitleRes = R.string.statistic_top_card_floating_active_subtitle,
                bodyRes = R.string.statistic_top_card_floating_active_body,
                bodyArg = counts[favoriteIdx],
                characterIconRes = favorite.iconRes(),
                fromName = favorite.koreanName(),
            )

            // 4-B. 그 외 — 캐릭터 빈도 데이터 없음
            else -> fallbackTopCard(nickname)
        }
    }

    private fun fallbackTopCard(nickname: String) = TopCardState(
        case = TopCardCase.FLOATING_EMPTY,
        nickname = nickname,
        subtitleRes = R.string.statistic_top_card_floating_empty_subtitle,
        bodyRes = R.string.statistic_top_card_floating_empty_body,
        characterIconRes = CharacterName.KIKI.iconRes(),
        fromName = null,
    )

    private fun loadTimerTimes() = launch {
        getTimerTimesUseCase().onSuccess { records ->
            timerTimes = records
            updateState {
                it.copy(
                    focusTime = buildFocusCard(it.focusTime),
                    restTime = buildRestCard(it.restTime),
                )
            }
        }
    }

    /** 집중 카드: 선택 기간의 NORMAL+STUDY+EXERCISE 합을 총합으로, 각 타입을 breakdown 으로. */
    private fun buildFocusCard(card: StatisticTimeCardState): StatisticTimeCardState {
        val timeType = card.selectedPeriod.toTimeType()
        val normal = timerTimes.concentrateTime("NORMAL", timeType)
        val study = timerTimes.concentrateTime("STUDY", timeType)
        val exercise = timerTimes.concentrateTime("EXERCISE", timeType)
        val total = normal + study + exercise
        return card.copy(
            hour = hourText(total),
            minute = minuteText(total),
            second = secondText(total),
            breakdown = listOf(
                StatisticModeBreakdownState(iconRes = null, label = "일반모드", timeText = formatCompact(normal)),
                StatisticModeBreakdownState(iconRes = R.drawable.ic_pencil, label = "공부모드", timeText = formatCompact(study)),
                StatisticModeBreakdownState(iconRes = R.drawable.ic_basketball, label = "운동모드", timeText = formatCompact(exercise)),
            ),
        )
    }

    /** 휴식 카드: 선택 기간의 REST 총 시간(모드별 breakdown 없음). */
    private fun buildRestCard(card: StatisticTimeCardState): StatisticTimeCardState {
        val restSeconds = timerTimes.restTime(card.selectedPeriod.toTimeType())
        return card.copy(
            hour = hourText(restSeconds),
            minute = minuteText(restSeconds),
            second = secondText(restSeconds),
        )
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
                    dates = calendarDates(newYear, newMonth),
                ),
                focusTime = it.focusTime.copy(periodLabel = newPeriodLabel),
                restTime = it.restTime.copy(periodLabel = newPeriodLabel),
                characterRank = it.characterRank.copy(periodLabel = newPeriodLabel),
            )
        }
        loadCalendar(yearMonthKey(newYear, newMonth))
    }

    /**
     * 월 변경마다 호출. 응답(3개월)을 map 에 누적 후 "현재 표시 중인 달"을 다시 그린다.
     * 연타 시: 표시 월은 shiftMonth 로 이미 갱신됐고, 어떤 응답이 늦게 와도 map 병합 후
     * 현재 달 기준으로 재구성 → 최종적으로 현재 달 데이터가 반영된다.
     */
    private fun loadCalendar(yearMonth: String) = launch {
        getCalendarUseCase(yearMonth).onSuccess { months ->
            calendarMonths = calendarMonths + months.associateBy { normalizeYearMonth(it.yearMonth) }
            updateState {
                it.copy(calendar = it.calendar.copy(dates = calendarDates(it.calendar.year, it.calendar.month)))
            }
        }
    }

    /** 그 달 그리드 생성 후, 받아둔 데이터로 현재 달 날짜에 레벨/불꽃을 입힌다. */
    private fun calendarDates(year: Int, month: Int): List<StatisticDateUiModel> {
        val base = generateCalendarDates(year, month)
        val monthData = calendarMonths[yearMonthKey(year, month)] ?: return base
        val prevData = calendarMonths[shiftedYearMonthKey(year, month, -1)]
        val nextData = calendarMonths[shiftedYearMonthKey(year, month, 1)]
        return base.map { date ->
            if (!date.isCurrentMonth) return@map date
            val (level, isFever) = dayLevel(monthData, date.day, prevData, nextData)
            date.copy(level = level, isFeverDay = isFever)
        }
    }

    companion object {
        private const val STREAK_THRESHOLD = 2
        private const val RETURN_MIN_DAYS = 2
        private const val RETURN_MAX_DAYS = 7
    }
}

private fun StatisticPeriod.toTimeType(): String = when (this) {
    StatisticPeriod.Month -> "month"
    StatisticPeriod.Week -> "week"
    StatisticPeriod.Day -> "day"
}

// 스펙 오타 "CONCENRATE" 와 코드 컨벤션 "CONCENTRATE" 모두 허용.
private fun TimerTimeRecord.isConcentrate(): Boolean = mode == "CONCENTRATE" || mode == "CONCENRATE"

private fun List<TimerTimeRecord>.concentrateTime(type: String, timeType: String): Long =
    firstOrNull { it.timeType == timeType && it.isConcentrate() && it.concentrateType == type }?.time ?: 0L

private fun List<TimerTimeRecord>.restTime(timeType: String): Long =
    firstOrNull { it.timeType == timeType && it.mode == "REST" }?.time ?: 0L

private fun hourText(seconds: Long): String = "%02d".format(seconds / 3600)
private fun minuteText(seconds: Long): String = "%02d".format((seconds % 3600) / 60)
private fun secondText(seconds: Long): String = "%02d".format(seconds % 60)

private fun formatCompact(seconds: Long): String {
    if (seconds <= 0L) return "0초"
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    val parts = buildList {
        if (hours > 0) add("${hours}시간")
        if (minutes > 0) add("${minutes}분")
        if (secs > 0) add("${secs}초")
    }
    return parts.joinToString(" ")
}

private fun yearMonthKey(year: Int, month: Int): String = "%04d-%02d".format(year, month)

private fun shiftedYearMonthKey(year: Int, month: Int, delta: Int): String {
    val total = year * 12 + (month - 1) + delta
    return yearMonthKey(total / 12, total % 12 + 1)
}

private fun normalizeYearMonth(raw: String): String {
    val parts = raw.split("-")
    val year = parts.getOrNull(0)?.toIntOrNull()
    val month = parts.getOrNull(1)?.toIntOrNull()
    return if (year != null && month != null) yearMonthKey(year, month) else raw
}

/** 하루의 색 레벨 + 불꽃 여부. 노랑(Low)=고립 기록, 주황(Medium)=2일 이상 연속(앞/뒤 달 경계 포함). */
private fun dayLevel(
    month: CalendarMonth,
    day: Int,
    prev: CalendarMonth?,
    next: CalendarMonth?,
): Pair<StatisticLevel, Boolean> {
    val hasRecord = month.dayRecord.getOrNull(day - 1) == true
    if (!hasRecord) return StatisticLevel.None to false

    val prevRecord = if (day > 1) {
        month.dayRecord.getOrNull(day - 2) == true
    } else {
        prev?.dayRecord?.lastOrNull() == true
    }
    val nextRecord = if (day < month.dayRecord.size) {
        month.dayRecord.getOrNull(day) == true
    } else {
        next?.dayRecord?.firstOrNull() == true
    }
    val level = if (prevRecord || nextRecord) StatisticLevel.Medium else StatisticLevel.Low
    return level to (month.feverDay == day)
}

/** lastAttendanceDate("yyyy-MM-dd")로부터 오늘까지의 경과 일수. 파싱 실패/없음이면 null. */
private fun daysSince(dateStr: String?): Int? {
    if (dateStr.isNullOrBlank()) return null
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val last = formatter.parse(dateStr) ?: return null
        val today = formatter.parse(formatter.format(Date())) ?: return null
        ((today.time - last.time) / (1000L * 60 * 60 * 24)).toInt()
    } catch (e: Exception) {
        null
    }
}

@DrawableRes
private fun CharacterName.iconRes(): Int = when (this) {
    CharacterName.KIKI -> R.drawable.ic_positive_kiki
    CharacterName.BOBO -> R.drawable.ic_positive_bobo
    CharacterName.NANA -> R.drawable.ic_positive_nana
    CharacterName.CHACHA -> R.drawable.ic_positive_chacha
    CharacterName.BOOBOO -> R.drawable.ic_positive_booboo
}

private fun CharacterName.koreanName(): String = when (this) {
    CharacterName.KIKI -> "키키"
    CharacterName.BOBO -> "보보"
    CharacterName.NANA -> "나나"
    CharacterName.CHACHA -> "차차"
    CharacterName.BOOBOO -> "부부"
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
        val isFuture = year > todayYear ||
            (year == todayYear && month > todayMonth) ||
            (year == todayYear && month == todayMonth && d > todayDay)
        dates.add(StatisticDateUiModel(
            day = d,
            isCurrentMonth = true,
            isSelected = false,
            isToday = year == todayYear && month == todayMonth && d == todayDay,
            isFuture = isFuture,
        ))
    }

    val remaining = (7 - dates.size % 7) % 7
    for (d in 1..remaining) {
        dates.add(StatisticDateUiModel(day = d, isCurrentMonth = false, isSelected = false, isToday = false))
    }

    return dates
}
