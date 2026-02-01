package domain.usecase.photo

import data.local.repository.UnsplashImageLocalRepository
import data.mapper.toUnsplashPhotoLocal
import data.remote.model.UnsplashPhotoRemote
import data.remote.repository.Resource

class DeletePhotoUseCase(private val cacheRepository: UnsplashImageLocalRepository) {
    suspend operator fun invoke(photo: UnsplashPhotoRemote): Resource<Int> {
        return cacheRepository.deletePhoto(photo.toUnsplashPhotoLocal())
    }
}
