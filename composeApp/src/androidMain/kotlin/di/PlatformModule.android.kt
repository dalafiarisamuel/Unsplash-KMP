package di

import com.tamuno.unsplash.kmp.widget.AndroidPhotosWidgetUpdater
import com.tamuno.unsplash.kmp.worker.PhotosWidgetWorker
import data.local.database.DatabaseFactory
import data.local.preference.SettingsPreferenceFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.Module
import org.koin.dsl.module
import ui.download.PlatformDownloadImage
import ui.widget.PhotosWidgetUpdater

internal actual fun platformModule(): Module {
    return module {
        single { PlatformDownloadImage(androidApplication()) }
        single { DatabaseFactory(androidApplication()) }
        single { SettingsPreferenceFactory(androidApplication()) }
        single<PhotosWidgetUpdater> { AndroidPhotosWidgetUpdater(androidApplication()) }
        worker { PhotosWidgetWorker(get(), get(), get()) }
    }
}
