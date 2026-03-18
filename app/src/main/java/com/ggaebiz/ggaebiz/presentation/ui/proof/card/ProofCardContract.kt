package com.ggaebiz.ggaebiz.presentation.ui.proof.card

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap

data class ProofCardState(
    val hour: Int = 0,
    val minute: Int = 0,
    val characterImageResId: Int = 0,
    val isSaveMode: Boolean = false,
    val previewBitmap: ImageBitmap? = null,
)

sealed interface ProofCardIntent {
    data class EnterScreen(val context: Context) : ProofCardIntent
    data class CreateProofCardBitmap(val bitmap: ImageBitmap) : ProofCardIntent
    data object ClickSaveImage : ProofCardIntent
    data object ClickShareImage : ProofCardIntent
    data object ClickBack : ProofCardIntent
    data object LongPressImage : ProofCardIntent
}

sealed interface ProofCardSideEffect {
    data object NavigateBack : ProofCardSideEffect
    data class ShowToast(val msgResId: Int) : ProofCardSideEffect
    data class ShareImage(val uri: android.net.Uri) : ProofCardSideEffect
    data object SaveImageSuccess : ProofCardSideEffect
    data class SaveImageFailure(val msgResId: Int) : ProofCardSideEffect
}
