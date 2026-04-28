package com.ggaebiz.ggaebiz.presentation.ui.proof.editor

import androidx.compose.ui.graphics.Color
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizColorScheme
import com.ggaebiz.ggaebiz.presentation.model.TimeStampStyleType

data class TimeStampColor(
    val textColor: Color,
    val backgroundColor: Color? = null,
) {
    companion object {
        val ORANGE_PILL = TimeStampColor(textColor = GaeBizColorScheme.white, backgroundColor = GaeBizColorScheme.primaryOrange)
        val WHITE_PILL = TimeStampColor(textColor = GaeBizColorScheme.white, backgroundColor = GaeBizColorScheme.white16)

        val GRAY_OPACITY_PILL = TimeStampColor(textColor = GaeBizColorScheme.gray900, backgroundColor = GaeBizColorScheme.black20)
        val WHITE_OPACITY_PILL = TimeStampColor(textColor = GaeBizColorScheme.white, backgroundColor = GaeBizColorScheme.black40)

        val WHITE_TEXT = TimeStampColor(textColor = GaeBizColorScheme.white)
        val GRAY_TEXT = TimeStampColor(textColor = GaeBizColorScheme.gray900) }
}

sealed class TimeStampStyle {
    data class Zuume(val color: TimeStampColor) : TimeStampStyle()
    data class Poppins(val color: TimeStampColor) : TimeStampStyle()
    data class Digital(val color: TimeStampColor) : TimeStampStyle()
    data class Timeline(val color: TimeStampColor) : TimeStampStyle()
    data class Agbalumo(val color: TimeStampColor) : TimeStampStyle()
    data class PoppinsTimeFormat(val color: TimeStampColor) : TimeStampStyle()
    data class PoppinsClockFormat(val color: TimeStampColor) : TimeStampStyle()
}

fun TimeStampStyleType.toStyle(): TimeStampStyle = when (this) {
    TimeStampStyleType.ORANGE_ZUUME -> TimeStampStyle.Zuume(TimeStampColor.ORANGE_PILL)
    TimeStampStyleType.GRAY_ZUUME -> TimeStampStyle.Zuume(TimeStampColor.WHITE_PILL)

    TimeStampStyleType.ORANGE_POPPINS -> TimeStampStyle.Poppins(TimeStampColor.ORANGE_PILL)
    TimeStampStyleType.GRAY_POPPINS -> TimeStampStyle.Poppins(TimeStampColor.WHITE_PILL)

    TimeStampStyleType.DARK_ZUUME -> TimeStampStyle.Zuume(TimeStampColor.GRAY_OPACITY_PILL)
    TimeStampStyleType.DARK_DIGITAL -> TimeStampStyle.Digital(TimeStampColor.WHITE_OPACITY_PILL)

    TimeStampStyleType.WIHTE_TIMELINE -> TimeStampStyle.Timeline(TimeStampColor.WHITE_TEXT)
    TimeStampStyleType.GRAY_TIMELINE -> TimeStampStyle.Timeline(TimeStampColor.GRAY_TEXT)


    TimeStampStyleType.WHITE_AGBALUMO -> TimeStampStyle.Agbalumo(TimeStampColor.WHITE_TEXT)
    TimeStampStyleType.GRAY_AGBALUMO -> TimeStampStyle.Agbalumo(TimeStampColor.GRAY_TEXT)

    TimeStampStyleType.ORANGE_POPPINS_TIME_FORMAT -> TimeStampStyle.PoppinsTimeFormat(TimeStampColor.ORANGE_PILL)
    TimeStampStyleType.GRAY_POPPINS_TIME_FORMAT -> TimeStampStyle.PoppinsTimeFormat(TimeStampColor.WHITE_PILL)

    TimeStampStyleType.WHITE_POPPINS_TIME_CLOCK_FORMAT -> TimeStampStyle.PoppinsClockFormat(TimeStampColor.WHITE_TEXT)
    TimeStampStyleType.GRAY_POPPINS_TIME_CLOCK_FORMAT -> TimeStampStyle.PoppinsClockFormat(TimeStampColor.GRAY_TEXT)
}
