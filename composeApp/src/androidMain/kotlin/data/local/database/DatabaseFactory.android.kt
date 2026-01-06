package data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    val context: Context
) {
    actual fun create(): RoomDatabase.Builder<UnsplashPhotoDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(UnsplashPhotoDatabase.DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}