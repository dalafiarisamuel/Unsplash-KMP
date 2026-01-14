@file:OptIn(ExperimentalForeignApi::class)

package data.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import data.local.database.UnsplashPhotoDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class SettingsPreferenceFactory {

    actual fun createDataStore(): DataStore<Preferences> {

        val subfolderPath = "${documentDirectory()}/${UnsplashPhotoDatabase.FOLDER_NAME}"

        createDirectoryIfNeeded(subfolderPath)

        val preferenceFile = "$subfolderPath/${DATA_STORE_FILE_NAME}"

        return createDataStoreWithPath(producePath = { preferenceFile })
    }

    private fun documentDirectory(): String {
        val documentDirectory =
            NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
        return requireNotNull(documentDirectory?.path)
    }

    private fun createDirectoryIfNeeded(path: String) {
        val fileManager = NSFileManager.defaultManager
        if (!fileManager.fileExistsAtPath(path)) {
            fileManager.createDirectoryAtPath(
                path = path,
                withIntermediateDirectories = true,
                attributes = null,
                error = null,
            )
        }
    }
}
