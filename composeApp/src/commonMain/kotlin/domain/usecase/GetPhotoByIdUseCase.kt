package domain.usecase

import data.local.model.UnsplashPhotoLocal
import data.local.repository.UnsplashImageLocalRepository
import data.remote.repository.Resource

class GetPhotoByIdUseCase(private val cacheRepository: UnsplashImageLocalRepository) {
    suspend operator fun invoke(id: String): Resource<UnsplashPhotoLocal?> {
        return cacheRepository.getPhotoById(id)
    }
}