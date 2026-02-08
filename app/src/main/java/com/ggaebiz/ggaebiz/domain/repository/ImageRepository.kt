package com.ggaebiz.ggaebiz.domain.repository

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize
import com.ggaebiz.ggaebiz.presentation.model.Sticker

interface ImageRepository {

    suspend fun createCachedImage(
        background: ImageBitmap,
        canvasSize: IntSize,
        stickers: List<Sticker>
    ): Uri

    suspend fun saveImageToGallery(uri: Uri): Result<Unit>
    
    suspend fun createProofCard(
        bitmap: ImageBitmap
    ): Uri
}
