package data.repository

import data.model.remote.UnsplashPhotoRemote
import data.model.remote.UnsplashResponseRemote

interface ImageRepository {
    suspend fun getImageSearchResult(
        query: String,
        page: Int,
        loadSize: Int
    ): UnsplashResponseRemote

    suspend fun getPhoto(photoId: String): Resource<UnsplashPhotoRemote>
}