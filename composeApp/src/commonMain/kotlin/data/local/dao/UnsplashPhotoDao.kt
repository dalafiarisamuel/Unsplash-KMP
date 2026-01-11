package data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import data.local.model.UnsplashPhotoLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface UnsplashPhotoDao {

    @Upsert
    suspend fun insertPhoto(photoLocal: UnsplashPhotoLocal)

    @Query("SELECT * FROM UnsplashPhotoLocal ORDER BY `databaseCreatedDate` DESC")
    fun getAllPhotoAsFlow(): Flow<List<UnsplashPhotoLocal>>

    @Query("SELECT * FROM UnsplashPhotoLocal WHERE id=:id")
    suspend fun getPhotoById(id: String): UnsplashPhotoLocal?

    @Query("DELETE FROM UnsplashPhotoLocal WHERE id=:id")
    suspend fun deletePhoto(id: String): Int

    @Query("DELETE FROM UnsplashPhotoLocal")
    suspend fun clearAllPhotos()
}