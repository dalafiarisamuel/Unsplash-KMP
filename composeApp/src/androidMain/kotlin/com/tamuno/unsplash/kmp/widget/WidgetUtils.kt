package com.tamuno.unsplash.kmp.widget

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun decodeSampledBitmap(path: String, reqWidth: Int): Bitmap? {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }

    BitmapFactory.decodeFile(path, options)

    options.inSampleSize = calculateInSampleSize(options, reqWidth)
    options.inJustDecodeBounds = false
    // Use RGB_565 for 50% memory saving
    options.inPreferredConfig = Bitmap.Config.RGB_565

    return BitmapFactory.decodeFile(path, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int): Int {
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

data class WidgetPhoto(val id: String, val path: String)
