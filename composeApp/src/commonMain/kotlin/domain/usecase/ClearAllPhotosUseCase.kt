package domain.usecase

import data.local.repository.UnsplashImageLocalRepository
import data.remote.repository.Resource

class ClearAllPhotosUseCase(private val cacheRepository: UnsplashImageLocalRepository) {
    suspend operator fun invoke(): Resource<Unit> {
        return cacheRepository.clearAllPhotos()
    }
}
