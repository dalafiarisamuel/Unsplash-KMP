package data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {

    actual fun create(): RoomDatabase.Builder<UnsplashPhotoDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")

        val appDataDir = when {
            os.contains("win") -> File(System.getenv("AppData"))
            os.contains("mac") -> File(
                userHome,
                "Library/Application Support/${UnsplashPhotoDatabase.FOLDER_NAME}"
            )

            else -> File(
                userHome,
                ".local/share/${UnsplashPhotoDatabase.FOLDER_NAME}"
            ) // Linux/Unix
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, UnsplashPhotoDatabase.DB_NAME)
        return Room.databaseBuilder<UnsplashPhotoDatabase>(
            name = dbFile.absolutePath,
        )
    }
}