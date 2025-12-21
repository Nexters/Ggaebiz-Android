package com.ggaebiz.ggaebiz.presentation.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface Sticker {
    val id: String
    val x: Float
    val y: Float
    val scale: Float
    val zIndex: Int
    val isSelected: Boolean
    var rotation: Float
    val source: StickerSource
}

data class BitmapSticker(
    override val id: String = java.util.UUID.randomUUID().toString(),
    override val x: Float,
    override val y: Float,
    override val scale: Float = 1f,
    override val zIndex: Int = 0,
    override val isSelected: Boolean = false,
    override var rotation: Float = 0f,
    override val source: StickerSource.Bitmap
) : Sticker

sealed interface StickerSource {
    data class Png(@DrawableRes val resId: Int): StickerSource
    data class Bitmap(val image: ImageBitmap) : StickerSource
}
