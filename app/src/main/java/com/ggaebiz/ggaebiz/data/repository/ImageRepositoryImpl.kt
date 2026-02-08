package com.ggaebiz.ggaebiz.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.IntSize
import androidx.core.content.FileProvider
import com.ggaebiz.ggaebiz.domain.repository.ImageRepository
import com.ggaebiz.ggaebiz.presentation.model.Sticker
import com.ggaebiz.ggaebiz.presentation.model.StickerSource
import com.ggaebiz.ggaebiz.presentation.ui.proof.loadBitmapFromUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageRepositoryImpl(
    private val appContext: Context
) : ImageRepository {

    override suspend fun createCachedImage(
        background: ImageBitmap,
        canvasSize: IntSize,
        stickers: List<Sticker>
    ): Uri {

        val backgroundBitmap = background.asAndroidBitmap()

        val mergedBitmap = withContext(Dispatchers.Default) {
            createShareBitmap(
                background = backgroundBitmap,
                canvasSize = canvasSize,
                stickers = stickers
            )
        }

        return withContext(Dispatchers.IO) {
            saveBitmapToCache(mergedBitmap)
        }
    }

    override suspend fun saveImageToGallery(uri: Uri): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                saveBitmapToGallery(uri)
            }
        }

    override suspend fun createProofCard(
        bitmap: ImageBitmap
    ): Uri = withContext(Dispatchers.IO) {
        val androidBitmap = bitmap.asAndroidBitmap()
        saveBitmapToCache(androidBitmap)
    }


    private fun createShareBitmap(
        background: Bitmap,
        canvasSize: IntSize,
        stickers: List<Sticker>
    ): Bitmap {

        val result = Bitmap.createBitmap(
            background.width,
            background.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(result)
        canvas.drawBitmap(background, 0f, 0f, null)

        val scaleFactor =
            background.width.toFloat() / canvasSize.width.toFloat()

        stickers
            .sortedBy { it.zIndex }
            .forEach { sticker ->
                when(sticker.source){
                    is StickerSource.Bitmap -> {
                        val stickerBitmap = (sticker.source as StickerSource.Bitmap).image.asAndroidBitmap()
                        val cx = sticker.x * scaleFactor
                        val cy = sticker.y * scaleFactor

                        val matrix = Matrix().apply {
                            postTranslate(
                                -stickerBitmap.width / 2f,
                                -stickerBitmap.height / 2f
                            )
                            postScale(
                                sticker.scale * scaleFactor,
                                sticker.scale * scaleFactor
                            )
                            postRotate(sticker.rotation)
                            postTranslate(cx, cy)
                        }
                        canvas.drawBitmap(stickerBitmap, matrix, null)
                    }
                    else-> {}
                }
            }

        return result
    }

    /**
     * cacheDir 에 파일 저장
     */
    private fun saveBitmapToCache(bitmap: Bitmap): Uri {
        val file = File(
            appContext.cacheDir,
            "editor_${System.currentTimeMillis()}.png"
        )

        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        return FileProvider.getUriForFile(
            appContext,
            "${appContext.packageName}.provider",
            file
        )
    }

    /**
     * 갤러리에 저장 (Android Q+ 기준)
     */
    private fun saveBitmapToGallery(uri: Uri) {
        val resolver = appContext.contentResolver

        val fileName = "ggaebiz_${System.currentTimeMillis()}.jpg"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/GaeBiz"
            )
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection =
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val galleryUri =
            resolver.insert(collection, values)
                ?: error("MediaStore insert failed")

        resolver.openInputStream(uri).use { input ->
            resolver.openOutputStream(galleryUri).use { output ->
                requireNotNull(input)
                requireNotNull(output)
                input.copyTo(output)
            }
        }

        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(galleryUri, values, null, null)
    }

}
