package domain.usecase

import data.local.repository.UnsplashImageLocalRepository
import data.mapper.toUnsplashPhotoLocal
import data.remote.model.UnsplashPhotoRemote
import data.remote.repository.Resource

class SavePhotoUseCase(private val cacheRepository: UnsplashImageLocalRepository) {
    suspend operator fun invoke(photo: UnsplashPhotoRemote): Resource<Unit> {
        return cacheRepository.insertPhoto(photo.toUnsplashPhotoLocal())
    }
}