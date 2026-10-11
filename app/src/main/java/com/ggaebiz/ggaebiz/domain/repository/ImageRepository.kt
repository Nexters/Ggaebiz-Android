package com.ggaebiz.ggaebiz.domain.repository

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap

interface ImageRepository {

    suspend fun createCachedImage(bitmap: ImageBitmap): Uri

    suspend fun saveImageToGallery(uri: Uri): Result<Unit>
}
