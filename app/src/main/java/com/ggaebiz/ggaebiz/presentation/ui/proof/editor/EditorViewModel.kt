package com.ggaebiz.ggaebiz.presentation.ui.proof.editor

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ggaebiz.ggaebiz.domain.usecase.CreateCachedImageUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.model.BitmapSticker
import com.ggaebiz.ggaebiz.presentation.model.Sticker
import com.ggaebiz.ggaebiz.presentation.model.StickerSource
import com.ggaebiz.ggaebiz.presentation.ui.proof.loadBitmapFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(
    savedStateHandle: SavedStateHandle,
    private val createCachedImageUseCase: CreateCachedImageUseCase
) : BaseViewModel<EditorState, EditorIntent, EditorEffect>(
    EditorState(
        imageUri = savedStateHandle.get<String>("uri")?.let(Uri::parse)
    )
) {
    fun processIntent(intent: EditorIntent) {
        when (intent) {
            is EditorIntent.OnLoadImageData -> {
                viewModelScope.launch {
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
            }

            EditorIntent.ClickBack -> {
            }

            is EditorIntent.ClickFinish -> {
                viewModelScope.launch {
                    val uri = uiState.value.previewBitmap?.let {
                        createCachedImageUseCase(
                            background = it,
                            canvasSize = intent.canvasSize,
                            stickers = uiState.value.stickers
                        )
                    }
                    uri?.let { EditorEffect.NavigateToSaveImage(it) }?.let { postSideEffect(it) }
                }
            }

            is EditorIntent.ChangeTab -> {
                updateState { it.copy(selectTab = intent.tab) }
            }

            is EditorIntent.OnPick -> {
                val center = Offset(
                    intent.canvasSize.width / 2f,
                    intent.canvasSize.height / 2f
                )

                when (intent.source) {
                    is StickerSource.Png -> {
                        val imageBitmap = BitmapFactory
                            .decodeResource(intent.resource, intent.source.resId)
                            .asImageBitmap()

                        updateState { state ->
                            state.copy(
                                stickers = state.stickers + BitmapSticker(
                                    offset = center,
                                    source = StickerSource.Bitmap(imageBitmap),
                                    zIndex = (state.stickers.maxOfOrNull { it.zIndex } ?: 0) + 1,
                                    x = center.x,
                                    y = center.y
                                )
                            )
                        }
                    }
                    else -> Unit
                }
            }

            is EditorIntent.OnTransform -> {
                updateState { state ->
                    state.copy(
                        stickers = state.stickers.map { s ->
                            if (s.id != intent.id) s
                            else when (s) {
                                is BitmapSticker -> s.copy(
                                    offset = s.offset + intent.pan,
                                    scale = intent.zoom ?: s.scale,
                                    rotation = intent.rotation ?: s.rotation
                                )
                            }
                        }
                    )
                }
            }



            is EditorIntent.OnSelectImage -> {
                updateState { it.copy(selectImageId = intent.selectImageId) }
            }

            is EditorIntent.OnMove -> {
                updateState {
                    it.copy(stickers = uiState.value.stickers.map {
                        if (it.id == intent.id) it.withPos(
                            intent.x,
                            intent.y
                        ) else it
                    })
                }
            }

            is EditorIntent.OnRemove -> {
                updateState { it.copy(stickers = uiState.value.stickers.filterNot { it.id == intent.id })}
                if (uiState.value.selectImageId == intent.id) {
                    updateState { it.copy(selectImageId = null) }
                }
            }
            is EditorIntent.OnResize -> {
                updateState { state ->
                    state.copy(stickers = uiState.value.stickers.map {
                        if (it.id == intent.id) it.withScale(
                            intent.scale
                        ) else it
                    })
                }
            }
            is EditorIntent.OnRotate -> {
                updateState { state ->
                    state.copy(stickers = uiState.value.stickers.map {
                        if (it.id == intent.id) it.withRotation(
                            it.rotation + intent.newRotation
                        ) else it
                    })
                }
            }
            is EditorIntent.OnBringToFront -> {
                val newTop = (uiState.value.stickers.maxOfOrNull { it.zIndex } ?: 0) + 1
                updateState { state ->
                    state.copy(stickers = uiState.value.stickers.map {
                        if (it.id == intent.id) it.withZ(
                            newTop
                        ) else it
                    })
                }
            }
        }
    }

    /** 스티커 조작용 확장 , 혹여 벡터를 체크해야하는 일이 올까봐 .. */
    private fun Sticker.withPos(x: Float, y: Float) = when (this) {
        is BitmapSticker -> copy(x = x, y = y)
    }
    private fun Sticker.withScale(scale: Float) = when (this) {
        is BitmapSticker -> copy(scale = scale.coerceIn(0.3f, 5f))
    }

    private fun Sticker.withZ(z: Int) = when (this) {
        is BitmapSticker -> copy(zIndex = z)
    }

    fun Sticker.withRotation(newRotation: Float): Sticker = when (this) {
        is BitmapSticker -> this.copy(rotation = newRotation)
    }
}
