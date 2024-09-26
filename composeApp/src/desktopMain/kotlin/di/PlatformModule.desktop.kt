package di

import org.koin.core.module.Module
import org.koin.dsl.module
import ui.download.PlatformDownloadImage

internal actual fun platformModule(): Module {
    return module {
        single { PlatformDownloadImage(get()) }
    }
}