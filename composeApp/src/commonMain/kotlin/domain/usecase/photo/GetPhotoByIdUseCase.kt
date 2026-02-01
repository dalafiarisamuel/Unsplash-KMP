package domain.usecase.photo

import data.local.model.UnsplashPhotoLocal
import data.local.repository.UnsplashImageLocalRepository
import data.remote.repository.Resource

class GetPhotoByIdUseCase(private val cacheRepository: UnsplashImageLocalRepository) {
    suspend operator fun invoke(id: String): Resource<UnsplashPhotoLocal?> {
        val result = cacheRepository.getPhotoById(id)
        if (result is Resource.Success && result.result == null) {
            return Resource.Failure(Exception("Photo not found"))
        }
        return cacheRepository.getPhotoById(id)
    }
}
