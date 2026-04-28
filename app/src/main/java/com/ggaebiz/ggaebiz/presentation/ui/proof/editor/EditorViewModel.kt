package com.ggaebiz.ggaebiz.presentation.ui.proof.editor

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import com.ggaebiz.ggaebiz.domain.usecase.CreateCachedImageUseCase
import com.ggaebiz.ggaebiz.domain.usecase.GetCurrentTimerUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.model.Sticker
import com.ggaebiz.ggaebiz.presentation.ui.proof.loadBitmapFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditorViewModel(
    savedStateHandle: SavedStateHandle,
    private val createCachedImageUseCase: CreateCachedImageUseCase,
    private val getCurrentTimerUseCase: GetCurrentTimerUseCase,
) : BaseViewModel<EditorState, EditorIntent, EditorEffect>(
    EditorState(
        imageUri = savedStateHandle.get<String>("uri")?.let(Uri::parse),
    )
) {
    init {
        launch {
            val (_, hour, minute, _) = getCurrentTimerUseCase()
            updateState { it.copy(timeStampResource = generateTimeStampSources(hour, minute)) }
        }
    }

    fun processIntent(intent: EditorIntent) {
        when (intent) {
            is EditorIntent.OnLoadImageData -> loadImageData(intent)
            is EditorIntent.ClickFinish -> finishEditing(intent)
            is EditorIntent.ClickBack -> {}
            is EditorIntent.ChangeTab -> updateState { it.copy(selectTab = intent.tab) }
            is EditorIntent.OnPick -> addSticker(intent)
            is EditorIntent.OnSelectImage -> selectSticker(intent)
            is EditorIntent.OnMove -> moveSticker(intent)
            is EditorIntent.OnRemove -> removeSticker(intent)
            is EditorIntent.OnScale -> scaleSticker(intent)
            is EditorIntent.OnRotate -> rotateSticker(intent)
        }
    }

    private fun loadImageData(intent: EditorIntent.OnLoadImageData) = launch {
        withContext(Dispatchers.IO) {
            runCatching {
                loadBitmapFromUri(
                    intent.context,
                    intent.imageUri,
                    reqWidth = null,
                    reqHeight = null
                ).asImageBitmap()
            }.onSuccess { bitmap ->
                updateState { it.copy(previewBitmap = bitmap) }
            }.onFailure {
                updateState { it.copy(isImageLoadError = true) }
            }
        }
    }

    private fun finishEditing(intent: EditorIntent.ClickFinish) = launch {
        val uri = createCachedImageUseCase(intent.capturedBitmap)
        postSideEffect(EditorEffect.NavigateToSaveImage(uri))
    }

    private fun addSticker(intent: EditorIntent.OnPick) {
        val center = Offset(
            intent.canvasSize.width / 2f,
            intent.canvasSize.height / 2f
        )
        updateState { state ->
            state.copy(
                stickers = state.stickers + Sticker(
                    offset = center,
                    source = intent.source,
                    zIndex = (state.stickers.maxOfOrNull { it.zIndex } ?: 0) + 1
                )
            )
        }
    }

    private fun selectSticker(intent: EditorIntent.OnSelectImage) {
        updateState { state ->
            val newTop = (state.stickers.maxOfOrNull { it.zIndex } ?: 0) + 1
            state.copy(
                selectImageId = intent.selectImageId,
                stickers = state.stickers.map { sticker ->
                    if (sticker.id == intent.selectImageId) sticker.copy(zIndex = newTop)
                    else sticker
                }
            )
        }
    }

    private fun moveSticker(intent: EditorIntent.OnMove) {
        updateState { state ->
            state.copy(stickers = state.stickers.map { sticker ->
                if (sticker.id == intent.id) sticker.copy(offset = Offset(intent.x, intent.y))
                else sticker
            })
        }
    }

    private fun removeSticker(intent: EditorIntent.OnRemove) {
        updateState { state ->
            state.copy(
                stickers = state.stickers.filterNot { it.id == intent.id },
                selectImageId = if (state.selectImageId == intent.id) null else state.selectImageId
            )
        }
    }

    private fun scaleSticker(intent: EditorIntent.OnScale) {
        updateState { state ->
            state.copy(stickers = state.stickers.map { sticker ->
                if (sticker.id == intent.id) sticker.copy(scale = intent.scale.coerceIn(0.3f, 5f))
                else sticker
            })
        }
    }

    private fun rotateSticker(intent: EditorIntent.OnRotate) {
        updateState { state ->
            state.copy(stickers = state.stickers.map { sticker ->
                if (sticker.id == intent.id) sticker.copy(
                    rotation = (sticker.rotation + intent.newRotation).mod(360f)
                )
                else sticker
            })
        }
    }

}
