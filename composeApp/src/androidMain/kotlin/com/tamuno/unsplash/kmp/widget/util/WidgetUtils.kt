package com.tamuno.unsplash.kmp.widget.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import com.tamuno.unsplash.kmp.widget.data.WidgetPhoto

private val bitmapCache: LruCache<String, Bitmap> by lazy {
    val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    val cacheSize = maxMemory / 8
    object : LruCache<String, Bitmap>(cacheSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount / 1024
        }
    }
}

fun decodeSampledBitmap(path: String, reqWidth: Int): Bitmap? {
    val cacheKey = "$path-$reqWidth"
    bitmapCache.get(cacheKey)?.let { return it }

    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }

    BitmapFactory.decodeFile(path, options)

    options.inSampleSize = calculateInSampleSize(options, reqWidth)
    options.inJustDecodeBounds = false
    // Use RGB_565 for 50% memory saving
    options.inPreferredConfig = Bitmap.Config.RGB_565

    return BitmapFactory.decodeFile(path, options)?.also {
        bitmapCache.put(cacheKey, it)
    }
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int): Int {
    val width = options.outWidth
    var inSampleSize = 1

    if (width > reqWidth) {
        val halfWidth = width / 2
        while (halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}

fun mapEntriesToPhotos(entries: Set<String>): List<WidgetPhoto> {
    return entries.mapNotNull {
        val parts = it.split("|")
        if (parts.size == 2) {
            WidgetPhoto(parts[0], parts[1])
        } else null
    }
}
