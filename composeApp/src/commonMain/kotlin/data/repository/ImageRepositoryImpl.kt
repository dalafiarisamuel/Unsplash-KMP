package data.repository

import data.model.remote.UnsplashPhotoRemote
import data.model.remote.UnsplashResponseRemote
import networking.ApiInterface

internal class ImageRepositoryImpl(private val api: ApiInterface) : ImageRepository {

    override suspend fun getImageSearchResult(
        query: String,
        page: Int,
        loadSize: Int,
    ): Resource<UnsplashResponseRemote> {
        return resourceHelper { api.searchPhotos(query = query, page = page, perPage = loadSize) }
    }

    override suspend fun getPhoto(photoId: String): Resource<UnsplashPhotoRemote> {
        return resourceHelper { api.getPhoto(photoId = photoId) }
    }
}