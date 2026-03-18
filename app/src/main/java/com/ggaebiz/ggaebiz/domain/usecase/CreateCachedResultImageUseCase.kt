package com.ggaebiz.ggaebiz.domain.usecase

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.ggaebiz.ggaebiz.domain.repository.ImageRepository

class CreateCachedImageUseCase(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(bitmap: ImageBitmap): Uri {
        return imageRepository.createCachedImage(bitmap)
    }
}
