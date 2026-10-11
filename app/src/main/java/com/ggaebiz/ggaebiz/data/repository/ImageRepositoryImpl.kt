package com.ggaebiz.ggaebiz.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import com.ggaebiz.ggaebiz.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageRepositoryImpl(
    private val appContext: Context
) : ImageRepository {

    override suspend fun createCachedImage(bitmap: ImageBitmap): Uri =
        withContext(Dispatchers.IO) {
            saveBitmapToCache(bitmap.asAndroidBitmap())
        }

    override suspend fun saveImageToGallery(uri: Uri): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                saveBitmapToGallery(uri)
            }
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

        val fileName = "ggaebiz_${System.currentTimeMillis()}.png"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
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
