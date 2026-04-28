package com.ggaebiz.ggaebiz.presentation.ui.proof.editor

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.model.Sticker
import com.ggaebiz.ggaebiz.presentation.model.StickerSource

data class EditorState(
    val selectTab : SelectTab = SelectTab.TIME_STAMP,
    val imageUri : Uri?,
    val previewBitmap : ImageBitmap? = null,
    val isImageLoadError : Boolean = false,
    val selectImageId : String? = null,
    val stickers : List<Sticker> = emptyList(),
    val stickerSources : List<StickerSource> =
        listOf<StickerSource>(
            StickerSource.Png(R.drawable.sticker_1),
            StickerSource.Png(R.drawable.sticker_2),
            StickerSource.Png(R.drawable.sticker_3),
            StickerSource.Png(R.drawable.sticker_4),
            StickerSource.Png(R.drawable.sticker_5),
            StickerSource.Png(R.drawable.sticker_6),
            StickerSource.Png(R.drawable.sticker_7),
            StickerSource.Png(R.drawable.sticker_8),
            StickerSource.Png(R.drawable.sticker_9),
            StickerSource.Png(R.drawable.sticker_10),
            StickerSource.Png(R.drawable.sticker_11),
            StickerSource.Png(R.drawable.sticker_12),
            StickerSource.Png(R.drawable.sticker_13),
            StickerSource.Png(R.drawable.sticker_14),
            StickerSource.Png(R.drawable.sticker_15),
            StickerSource.Png(R.drawable.sticker_16),
            StickerSource.Png(R.drawable.sticker_17),
            StickerSource.Png(R.drawable.sticker_18)
        ),
    val timeStampResource : List<StickerSource> = emptyList(),
){
    val nowSource = if (selectTab == SelectTab.STICKER) stickerSources else timeStampResource
}

sealed interface EditorIntent {
    data class OnLoadImageData(val context: Context, val imageUri: Uri) : EditorIntent
    data class ClickFinish(val capturedBitmap: ImageBitmap) : EditorIntent
    data object ClickBack : EditorIntent
    data class ChangeTab(val tab : SelectTab) : EditorIntent

    // 스티커 선택해서 추가
    data class OnPick(val source : StickerSource, val canvasSize : IntSize) : EditorIntent

    // 현재 핸들링하는 스티커로 선택 + 최상위로 이동
    data class OnSelectImage(val selectImageId: String) : EditorIntent
    data class OnMove(val id : String, val x : Float, val y : Float): EditorIntent
    data class OnRemove(val id : String): EditorIntent
    data class OnRotate(val id : String, val newRotation: Float): EditorIntent
    data class OnScale(val id: String, val scale: Float) : EditorIntent
}


sealed interface EditorEffect {
    data object NavigateBack : EditorEffect
    data class NavigateToSaveImage(val image : Uri) : EditorEffect
}

enum class SelectTab {
    TIME_STAMP,
    STICKER
}