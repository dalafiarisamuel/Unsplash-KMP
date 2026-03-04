package com.tamuno.unsplash.kmp.widget

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.tamuno.unsplash.kmp.worker.PhotosWidgetWorker
import ui.widget.PhotosWidgetUpdater

internal class AndroidPhotosWidgetUpdater(private val context: Context) : PhotosWidgetUpdater {
    override fun update() {
        WorkManager.getInstance(context)
            .enqueue(
                OneTimeWorkRequestBuilder<PhotosWidgetWorker>()
                    .build()
            )
    }
}
