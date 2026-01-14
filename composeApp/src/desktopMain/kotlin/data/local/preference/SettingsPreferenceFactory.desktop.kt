package data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import data.local.database.UnsplashPhotoDatabase
import java.io.File

actual class SettingsPreferenceFactory {

    actual fun createDataStore(): DataStore<Preferences> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")

        val appDataDir =
            when {
                os.contains("win") -> File(System.getenv("AppData"))
                os.contains("mac") ->
                    File(
                        userHome,
                        "Library/Application Support/${UnsplashPhotoDatabase.FOLDER_NAME}",
                    )

                else ->
                    File(
                        userHome,
                        ".local/share/${UnsplashPhotoDatabase.FOLDER_NAME}",
                    ) // Linux/Unix
            }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val preferenceFile = File(appDataDir, DATA_STORE_FILE_NAME)

        return createDataStoreWithPath { preferenceFile.absolutePath }
    }
}
