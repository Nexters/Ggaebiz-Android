package com.ggaebiz.ggaebiz.presentation.ui.proof.editor

import com.ggaebiz.ggaebiz.presentation.model.StickerSource
import com.ggaebiz.ggaebiz.presentation.model.TimeStampStyleType

fun generateTimeStampSources(hour: Int, minute: Int): List<StickerSource.TimeStamp> {
    val hmText = "%02d : %02d".format(hour, minute)
    val minuteText = "${hour}h ${minute}min"
    return listOf(
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.ORANGE_ZUUME),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.GRAY_ZUUME),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.ORANGE_POPPINS),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.GRAY_POPPINS),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.DARK_ZUUME),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.DARK_DIGITAL),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.WIHTE_TIMELINE),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.GRAY_TIMELINE),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.WHITE_AGBALUMO),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.GRAY_AGBALUMO),
        StickerSource.TimeStamp(text = minuteText, styleType = TimeStampStyleType.ORANGE_POPPINS_TIME_FORMAT),
        StickerSource.TimeStamp(text = minuteText, styleType = TimeStampStyleType.GRAY_POPPINS_TIME_FORMAT),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.WHITE_POPPINS_TIME_CLOCK_FORMAT),
        StickerSource.TimeStamp(text = hmText, styleType = TimeStampStyleType.GRAY_POPPINS_TIME_CLOCK_FORMAT),
    )
}
