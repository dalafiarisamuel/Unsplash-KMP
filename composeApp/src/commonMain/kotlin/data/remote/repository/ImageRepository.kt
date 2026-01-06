package data.remote.repository

import data.remote.model.UnsplashPhotoRemote
import data.remote.model.UnsplashResponseRemote


internal interface ImageRepository {
    suspend fun getImageSearchResult(
        query: String,
        page: Int,
        loadSize: Int
    ): Resource<UnsplashResponseRemote>

    suspend fun getPhoto(photoId: String): Resource<UnsplashPhotoRemote>
}