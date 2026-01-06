package data.repository


import data.remote.model.UnsplashPhotoRemote
import data.remote.model.UnsplashResponseRemote
import data.remote.repository.ImageRepository
import data.remote.repository.Resource
import data.remote.repository.resourceHelper
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