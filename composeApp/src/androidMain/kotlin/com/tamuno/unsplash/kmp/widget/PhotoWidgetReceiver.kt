package com.tamuno.unsplash.kmp.widget

import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.tamuno.unsplash.kmp.widget.ui.PhotosWidget
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.widget.PhotosWidgetUpdater

internal class PhotoWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    override val glanceAppWidget = PhotosWidget()

    private val widgetUpdater: PhotosWidgetUpdater by inject()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            widgetUpdater.update()
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        widgetUpdater.update()
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: android.appwidget.AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        widgetUpdater.update()
    }
}
