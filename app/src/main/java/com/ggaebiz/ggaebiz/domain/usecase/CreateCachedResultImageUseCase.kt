package com.ggaebiz.ggaebiz.domain.usecase

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize
import com.ggaebiz.ggaebiz.domain.repository.ImageRepository
import com.ggaebiz.ggaebiz.presentation.model.Sticker

class CreateCachedImageUseCase(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(
        background: ImageBitmap,
        canvasSize: IntSize,
        stickers: List<Sticker>
    ): Uri {
        return imageRepository.createCachedImage(
            background,
            canvasSize,
            stickers
        )
    }
    suspend fun createProofCard(
        bitmap: ImageBitmap
    ): Uri {
        return imageRepository.createProofCard(bitmap)
    }
}
