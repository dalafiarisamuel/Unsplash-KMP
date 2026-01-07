@file:OptIn(ExperimentalForeignApi::class)

package data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDirectory

actual class DatabaseFactory {

    actual fun create(): RoomDatabase.Builder<UnsplashPhotoDatabase> {

        val subfolderPath = "${documentDirectory()}/${UnsplashPhotoDatabase.FOLDER_NAME}"

        createDirectoryIfNeeded(subfolderPath)

        val dbFile = "$subfolderPath/${UnsplashPhotoDatabase.DB_NAME}"


        return Room.databaseBuilder<UnsplashPhotoDatabase>(
            name = dbFile
        )
    }



    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDirectory,
            appropriateForURL = null,
            create = false,
            error = null
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
                error = null
            )
        }
    }
}