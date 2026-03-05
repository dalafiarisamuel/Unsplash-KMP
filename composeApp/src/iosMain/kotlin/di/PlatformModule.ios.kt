package di

import data.local.database.DatabaseFactory
import data.local.preference.SettingsPreferenceFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import ui.download.PlatformDownloadImage
import ui.widget.IosPhotosWidgetUpdater
import ui.widget.PhotosWidgetUpdater

internal actual fun platformModule(): Module {
    return module {
        single { PlatformDownloadImage() }
        single { DatabaseFactory() }
        single { SettingsPreferenceFactory() }
        single<PhotosWidgetUpdater> { IosPhotosWidgetUpdater() }
    }
}
