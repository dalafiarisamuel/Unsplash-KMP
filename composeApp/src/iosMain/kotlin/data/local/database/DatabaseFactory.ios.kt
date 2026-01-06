package data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDirectory

actual class DatabaseFactory {

    actual fun create(): RoomDatabase.Builder<UnsplashPhotoDatabase> {
        val dbFile = documentDirectory() + "/${UnsplashPhotoDatabase.DB_NAME}"

        return Room.databaseBuilder<UnsplashPhotoDatabase>(
            name = dbFile
        )
    }

    @OptIn(ExperimentalForeignApi::class)
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
}