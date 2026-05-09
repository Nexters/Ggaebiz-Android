package com.ggaebiz.ggaebiz.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.geometry.Offset
import java.util.UUID

data class Sticker(
    val id: String = UUID.randomUUID().toString(),
    val offset: Offset,
    val scale: Float = 1f,
    val rotation: Float = 0f,
    val zIndex: Int = 0,
    val source: StickerSource
)

enum class TimeStampStyleType {
    ORANGE_ZUUME,
    GRAY_ZUUME,
    ORANGE_POPPINS,
    GRAY_POPPINS,
    DARK_ZUUME,
    DARK_DIGITAL,
    WIHTE_TIMELINE,
    GRAY_TIMELINE,
    WHITE_AGBALUMO,
    GRAY_AGBALUMO,
    ORANGE_POPPINS_TIME_FORMAT,
    GRAY_POPPINS_TIME_FORMAT,
    WHITE_POPPINS_TIME_CLOCK_FORMAT,
    GRAY_POPPINS_TIME_CLOCK_FORMAT,
}

sealed interface StickerSource {
    data class Png(@DrawableRes val resId: Int) : StickerSource
    data class TimeStamp(
        val text: String,
        val styleType: TimeStampStyleType,
    ) : StickerSource
}
