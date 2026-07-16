package com.ggaebiz.ggaebiz.presentation.ui.statistic

import androidx.annotation.DrawableRes
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.model.CharacterName
import com.ggaebiz.ggaebiz.domain.model.CalendarMonth
import com.ggaebiz.ggaebiz.domain.model.TimerTimeRecord
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

internal fun StatisticPeriod.toTimeType(): String = when (this) {
    StatisticPeriod.Month -> "month"
    StatisticPeriod.Week -> "week"
    StatisticPeriod.Day -> "day"
}

internal fun TimerTimeRecord.isConcentrate(): Boolean = mode == "CONCENTRATE"

internal fun List<TimerTimeRecord>.concentrateTime(type: String, timeType: String): Long =
    firstOrNull { it.timeType == timeType && it.isConcentrate() && it.concentrateType == type }?.time ?: 0L

internal fun List<TimerTimeRecord>.restTime(timeType: String): Long =
    firstOrNull { it.timeType == timeType && it.mode == "REST" }?.time ?: 0L

internal fun hourText(seconds: Long): String = String.format(Locale.US, "%02d", seconds / 3600)
internal fun minuteText(seconds: Long): String = String.format(Locale.US, "%02d", (seconds % 3600) / 60)
internal fun secondText(seconds: Long): String = String.format(Locale.US, "%02d", seconds % 60)

internal fun formatCompact(seconds: Long): String {
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

internal fun yearMonthKey(year: Int, month: Int): String = String.format(Locale.US, "%04d-%02d", year, month)

internal fun shiftedYearMonthKey(year: Int, month: Int, delta: Int): String {
    val total = year * 12 + (month - 1) + delta
    return yearMonthKey(total / 12, total % 12 + 1)
}

internal fun normalizeYearMonth(raw: String): String {
    val parts = raw.split("-")
    val year = parts.getOrNull(0)?.toIntOrNull()
    val month = parts.getOrNull(1)?.toIntOrNull()
    return if (year != null && month != null) yearMonthKey(year, month) else raw
}

/** 하루의 색 레벨 + 불꽃 여부. 노랑(Low)=고립 기록, 주황(Medium)=2일 이상 연속(앞/뒤 달 경계 포함). */
internal fun dayLevel(
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
internal fun daysSince(dateStr: String?): Int? {
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

/** 상단 카드용 캐릭터 아이콘(positive 표정). */
@DrawableRes
internal fun CharacterName.iconRes(): Int = when (this) {
    CharacterName.KIKI -> R.drawable.ic_positive_kiki
    CharacterName.BOBO -> R.drawable.ic_positive_bobo
    CharacterName.NANA -> R.drawable.ic_positive_nana
    CharacterName.CHACHA -> R.drawable.ic_positive_chacha
    CharacterName.BOOBOO -> R.drawable.ic_positive_booboo
}

/** 랭킹 카드용 캐릭터 아이콘(기본 레벨). */
@DrawableRes
internal fun CharacterName.rankIconRes(): Int = when (this) {
    CharacterName.KIKI -> R.drawable.ic_kiki_level1
    CharacterName.BOBO -> R.drawable.ic_bobo_level1
    CharacterName.NANA -> R.drawable.ic_nana_level1
    CharacterName.CHACHA -> R.drawable.ic_chacha_level1
    CharacterName.BOOBOO -> R.drawable.ic_booboo_level1
}

internal fun CharacterName.koreanName(): String = when (this) {
    CharacterName.KIKI -> "키키"
    CharacterName.BOBO -> "보보"
    CharacterName.NANA -> "나나"
    CharacterName.CHACHA -> "차차"
    CharacterName.BOOBOO -> "부부"
}

internal fun generateCalendarDates(year: Int, month: Int): List<StatisticDateUiModel> {
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
