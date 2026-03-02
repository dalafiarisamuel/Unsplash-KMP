package domain.usecase.photo

import data.local.repository.UnsplashImageLocalRepository
import kotlinx.coroutines.flow.Flow

class ObservePhotoCountUseCase(private val cacheRepository: UnsplashImageLocalRepository) {
    operator fun invoke(): Flow<Int> {
        return cacheRepository.observePhotoCount()
    }

}
