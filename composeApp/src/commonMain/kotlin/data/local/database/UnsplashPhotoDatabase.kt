package data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import data.local.dao.UnsplashPhotoDao
import data.local.model.UnsplashPhotoLocal

@Database(entities = [UnsplashPhotoLocal::class], version = 1)
@ConstructedBy(UnsplashPhotoDatabaseConstructor::class)
abstract class UnsplashPhotoDatabase : RoomDatabase() {

  abstract fun getPhotoDao(): UnsplashPhotoDao

  companion object {
    const val DB_NAME = "unsplash_photo.db"
    const val FOLDER_NAME = "UnsplashKMP"
  }
}

@Suppress("KotlinNoActualForExpect")
expect object UnsplashPhotoDatabaseConstructor : RoomDatabaseConstructor<UnsplashPhotoDatabase> {
  override fun initialize(): UnsplashPhotoDatabase
}
