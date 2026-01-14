package di

import data.local.database.DatabaseFactory
import data.local.preference.SettingsPreferenceFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import ui.download.PlatformDownloadImage

internal actual fun platformModule(): Module {
    return module {
        single { PlatformDownloadImage(androidApplication()) }
        single { DatabaseFactory(androidApplication()) }
        single { SettingsPreferenceFactory(androidApplication()) }
    }
}
