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
import com.tamuno.unsplash.kmp.widget.data.PHOTOS_KEY
import com.tamuno.unsplash.kmp.widget.data.TOTAL_FAVOURITES_KEY
import com.tamuno.unsplash.kmp.widget.ui.PhotosWidget
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
        return try {
            val allPhotos = getFavouritePhotosUseCase().first()
            val totalCount = allPhotos.size
            val photos = allPhotos.take(12)

            Log.d("PhotosWidget", "Widget reading ${photos.size} entries out of $totalCount")

            val cacheDir = File(applicationContext.cacheDir, "widget")
            if (!cacheDir.exists()) cacheDir.mkdirs()

            val imageLoader = ImageLoader(applicationContext)
            val entries = mutableSetOf<String>()

            for (photo in photos) {
                val request =
                    ImageRequest.Builder(applicationContext).data(photo.urls.small).size(400).build()

                val result = imageLoader.execute(request)

                if (result is SuccessResult) {
                    val bitmap = result.image.toBitmap()
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

            // Update state
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
                    mutablePrefs[TOTAL_FAVOURITES_KEY] = totalCount
                    mutablePrefs
                }
            }

            // Explicitly update all instances
            PhotosWidget().updateAll(applicationContext)
            
            // Clean up files that are no longer in the entries set
            val activePaths = entries.map { it.split("|").last() }.toSet()
            cacheDir.listFiles()?.forEach { file ->
                if (file.absolutePath !in activePaths) {
                    file.delete()
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("PhotosWidget", "Error in worker", e)
            Result.retry()
        }
    }
}
