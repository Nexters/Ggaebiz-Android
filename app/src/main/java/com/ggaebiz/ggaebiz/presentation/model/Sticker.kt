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

sealed interface StickerSource {
    data class Png(@DrawableRes val resId: Int) : StickerSource
}
