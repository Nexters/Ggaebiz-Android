package com.ggaebiz.ggaebiz.presentation.ui.proof.finish

import android.content.Context
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ggaebiz.ggaebiz.domain.usecase.SaveImageToGalleryUseCase
import com.ggaebiz.ggaebiz.presentation.common.base.BaseViewModel
import com.ggaebiz.ggaebiz.presentation.ui.proof.loadBitmapFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class EditorResultState(
    val imageUri: Uri?,
    val imageBitmap: ImageBitmap? = null,
    val isImageLoadError: Boolean = false,
)

sealed interface EditorResultIntent {
    data class EnterScreen(val context: Context, val imageUri: Uri) : EditorResultIntent
    data object ClickSaveImage : EditorResultIntent
    data object ClickShareImage : EditorResultIntent
}

sealed interface EditorResultSideEffect {
    data object NavigateBack : EditorResultSideEffect
    data object MoveToDeviceSetting : EditorResultSideEffect
    data class ShowToast(val msg: String) : EditorResultSideEffect
    data class ShareImage(val uri: Uri) : EditorResultSideEffect
}


class EditorResultViewModel(
    savedStateHandle: SavedStateHandle,
    private val saveImageToGalleryUseCase: SaveImageToGalleryUseCase,
) : BaseViewModel<EditorResultState, EditorResultIntent, EditorResultSideEffect>(
    EditorResultState(
        imageUri = savedStateHandle.get<String>("uri")?.let(Uri::parse)
    )
) {

    fun processIntent(intent: EditorResultIntent) {
        when (intent) {
            is EditorResultIntent.EnterScreen -> {
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
                            updateState { it.copy(imageBitmap = bitmap) }
                        }.onFailure {
                            updateState { it.copy(isImageLoadError = true) }
                        }
                    }
                }
            }

            EditorResultIntent.ClickSaveImage -> {
                if (uiState.value.imageUri == null) {
                    postSideEffect(EditorResultSideEffect.ShowToast("이미지를 불러오기에 실패했습니다."))
                    return
                }
                viewModelScope.launch {
                    saveImageToGalleryUseCase(uiState.value.imageUri!!)
                        .onSuccess {
                            postSideEffect(EditorResultSideEffect.ShowToast("이미지를 저장했어요"))
                        }
                        .onFailure {
                            postSideEffect(EditorResultSideEffect.ShowToast("이미지 저장을 실패했어요. 다시 시도해주세요"))
                        }
                }
            }

            EditorResultIntent.ClickShareImage -> {
                if (uiState.value.imageUri == null) {
                    postSideEffect(EditorResultSideEffect.ShowToast("이미지를 불러오기에 실패했습니다."))
                    return
                }
                viewModelScope.launch {
                    postSideEffect(EditorResultSideEffect.ShareImage(uiState.value.imageUri!!))
                }
            }
        }
    }
}
