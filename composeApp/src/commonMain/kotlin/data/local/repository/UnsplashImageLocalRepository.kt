package data.local.repository

import data.local.model.UnsplashPhotoLocal
import data.remote.repository.Resource
import kotlinx.coroutines.flow.Flow

interface UnsplashImageLocalRepository {
    suspend fun insertPhoto(photoLocal: UnsplashPhotoLocal): Resource<Unit>
    fun getAllPhotoAsFlow(): Flow<List<UnsplashPhotoLocal>>
    suspend fun getPhotoById(id: Long): Resource<UnsplashPhotoLocal>
    suspend fun deletePhoto(photoLocal: UnsplashPhotoLocal): Resource<Unit>
    suspend fun clearAllPhotos(): Resource<Unit>
}