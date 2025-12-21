package com.ggaebiz.ggaebiz.domain.usecase

import android.net.Uri
import com.ggaebiz.ggaebiz.domain.repository.ImageRepository

class SaveImageToGalleryUseCase(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(uri: Uri) : Result<Unit>{
        return imageRepository.saveImageToGallery(uri)
    }
}
