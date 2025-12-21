package com.ggaebiz.ggaebiz.presentation.ui.proof

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build


@SuppressLint("ObsoleteSdkInt")
fun loadBitmapFromUri(
    context: Context,
    uri: Uri,
    reqWidth: Int? = null,
    reqHeight: Int? = null,
): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        // API 28+
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            // 소프트웨어 비트맵 권장(편집/합성 고려 시)
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            // 필요 시 타깃 크기 지정(없으면 원본)
            if (reqWidth != null && reqHeight != null) {
                val (tw, th) = fitInside(
                    srcW = info.size.width, srcH = info.size.height,
                    maxW = reqWidth, maxH = reqHeight
                )
                decoder.setTargetSize(tw, th)
            }
            // decoder.isMutableRequired = true // 편집용으로 즉시 수정하려면 활성화
        }
    } else {
        // API 27-
        context.contentResolver.openInputStream(uri).use { input ->
            // 1) 원본 크기만 먼저 읽기
            val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(input, null, opts)
        }
        val (targetW, targetH) =
            if (reqWidth != null && reqHeight != null) reqWidth to reqHeight else optsWidthHeight(context, uri)

        val sample = calculateInSampleSize(
            options = BitmapFactory.Options().apply {
                // 위에서 읽은 bounds가 필요하지만, 간단히 target 기준으로만 샘플링 계산해도 충분
                outWidth = optsWidthHeight(context, uri).first
                outHeight = optsWidthHeight(context, uri).second
            },
            reqWidth = targetW, reqHeight = targetH
        )
        val decodeOpts = BitmapFactory.Options().apply {
            inJustDecodeBounds = false
            inPreferredConfig = Bitmap.Config.ARGB_8888
            inSampleSize = sample
        }
        context.contentResolver.openInputStream(uri).use { input2 ->
            BitmapFactory.decodeStream(input2, null, decodeOpts)
                ?: error("Decode failed")
        }
    }
}


private fun fitInside(srcW: Int, srcH: Int, maxW: Int, maxH: Int): Pair<Int, Int> {
    if (maxW <= 0 || maxH <= 0) return srcW to srcH
    val scale = minOf(maxW.toFloat() / srcW, maxH.toFloat() / srcH)
    return (srcW * scale).toInt().coerceAtLeast(1) to (srcH * scale).toInt().coerceAtLeast(1)
}

private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    val (height: Int, width: Int) = options.outHeight to options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        var halfHeight = height / 2
        var halfWidth = width / 2
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

// bounds만 다시 구하고 싶을 때(간단 버전)
private fun optsWidthHeight(context: Context, uri: Uri): Pair<Int, Int> {
    val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    context.contentResolver.openInputStream(uri).use {
        BitmapFactory.decodeStream(it, null, opts)
    }
    return (opts.outWidth.takeIf { it > 0 } ?: 1) to (opts.outHeight.takeIf { it > 0 } ?: 1)
}
