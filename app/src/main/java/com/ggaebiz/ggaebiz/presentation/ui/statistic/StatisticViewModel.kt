package com.ggaebiz.ggaebiz.presentation.ui.statistic

import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.ggaebiz.ggaebiz.domain.model.CalendarMonth
import com.ggaebiz.ggaebiz.domain.model.TimerTimeRecord
import com.ggaebiz.ggaebiz.domain.model.TopCardData
import com.ggaebiz.ggaebiz.domain.repository.NicknameRepository
import com.ggaebiz.ggaebiz.domain.usecase.GetCalendarUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetTimerTimesUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetTopCardDataUseCase
import com.ggaebiz.ggaebiz.domain.usecase.SelectCharacterIdxUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import java.util.Calendar

class StatisticViewModel(
    private val getTopCardDataUseCase: GetTopCardDataUseCase,
    private val nicknameRepository: NicknameRepository,
    private val getTimerTimesUseCase: GetTimerTimesUseCase,
    private val getCalendarUseCase: GetCalendarUseCase,
    private val selectCharacterIdxUseCase: SelectCharacterIdxUseCase,
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
    private var ctaCharacterIndex: Int = 0

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

            StatisticIntent.ClickStartTimer -> startTimerWithTopCharacter()
        }
    }

    private fun loadTopCard() = launch {
        val nickname = nicknameRepository.getNickname().orEmpty()
        getTopCardDataUseCase()
            .onSuccess { data ->
                updateState {
                    it.copy(
                        topCard = buildTopCard(data, nickname),
                        characterRank = buildCharacterRank(data.selectionCountList, it.characterRank),
                    )
                }
            }
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

    /** 서버 리스트(KIKI,BOBO,NANA,CHACHA,BOOBOO 순 카운트)를 내림차순 정렬해 1~5위 배치. 동점은 서버 순서 유지. */
    private fun buildCharacterRank(
        counts: List<Int>,
        current: StatisticCharacterRankState,
    ): StatisticCharacterRankState {
        val ranked = CharacterName.entries
            .mapIndexed { index, character -> character to (counts.getOrNull(index) ?: 0) }
            .sortedByDescending { it.second }
        val items = ranked.mapIndexed { index, (character, count) ->
            StatisticCharacterRankItemState(
                rank = index + 1,
                iconRes = character.rankIconRes(),
                name = character.koreanName(),
                countText = "$count 회",
            )
        }

        val topCharacter = ranked.firstOrNull()?.first
        ctaCharacterIndex = topCharacter?.ordinal ?: 0
        return current.copy(
            items = items,
            ctaCharacterName = topCharacter?.koreanName() ?: current.ctaCharacterName,
        )
    }

    /** CTA "○○랑 타이머 시작": 최애 캐릭터를 선택 저장 후 타이머 설정 화면으로 이동. */
    private fun startTimerWithTopCharacter() = launch {
        selectCharacterIdxUseCase(ctaCharacterIndex)
        postSideEffect(StatisticSideEffect.NavigateToSetting)
    }

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
