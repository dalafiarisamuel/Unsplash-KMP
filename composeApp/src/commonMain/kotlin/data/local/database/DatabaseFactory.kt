package data.local.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<UnsplashPhotoDatabase>
}