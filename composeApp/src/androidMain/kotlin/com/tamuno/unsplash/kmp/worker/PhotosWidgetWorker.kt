package com.tamuno.unsplash.kmp.worker

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.scale
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.toBitmap
import com.tamuno.unsplash.kmp.widget.PHOTOS_KEY
import com.tamuno.unsplash.kmp.widget.PhotosWidget
import domain.usecase.photo.GetAllPhotoAsFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

internal class PhotosWidgetWorker(
    context: Context,
    params: WorkerParameters,
    private val getFavouritePhotosUseCase: GetAllPhotoAsFlowUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val photos = getFavouritePhotosUseCase().first().take(10)

        Log.d("PhotosWidget", "Widget reading ${photos.size} entries")

        val cacheDir = File(applicationContext.cacheDir, "widget")
        if (!cacheDir.exists()) cacheDir.mkdirs()

        // Clean up old files to save space
        cacheDir.listFiles()?.forEach { it.delete() }

        val imageLoader = ImageLoader(applicationContext)
        val entries = mutableSetOf<String>()

        for (photo in photos) {

            val request =
                ImageRequest.Builder(applicationContext).data(photo.urls.small).size(400).build()

            val result = imageLoader.execute(request)

            if (result is SuccessResult) {

                val bitmap = result.image.toBitmap()

                // Resize to prevent TransactionTooLargeException and memory limits
                // 250px is sufficient for a 2-3 column widget grid
                val targetWidth = 250
                val targetHeight = (bitmap.height * (targetWidth.toFloat() / bitmap.width)).toInt()

                val resized = bitmap.scale(targetWidth, targetHeight)

                val file = File(cacheDir, "${photo.id}.jpg")

                withContext(Dispatchers.IO) {
                    FileOutputStream(file).use {
                        resized.compress(Bitmap.CompressFormat.JPEG, 80, it)
                    }
                }

                entries.add("${photo.id}|${file.absolutePath}")
            }
        }

        // NEW CORRECT STATE UPDATE (per GlanceId)

        val manager = GlanceAppWidgetManager(applicationContext)
        val glanceIds = manager.getGlanceIds(PhotosWidget::class.java)

        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context = applicationContext,
                glanceId = glanceId,
                definition = PreferencesGlanceStateDefinition,
            ) { prefs ->
                val mutablePrefs = prefs.toMutablePreferences()
                mutablePrefs[PHOTOS_KEY] = entries
                mutablePrefs
            }
        }

        // Trigger UI refresh
        PhotosWidget().updateAll(applicationContext)

        return Result.success()
    }
}
