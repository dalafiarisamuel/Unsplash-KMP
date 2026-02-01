package domain.usecase.photo

import data.local.repository.UnsplashImageLocalRepository
import data.mapper.toPhotoList
import data.ui.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllPhotoAsFlowUseCase(private val cacheRepository: UnsplashImageLocalRepository) {
    operator fun invoke(): Flow<List<Photo>> {
        return cacheRepository.getAllPhotoAsFlow().map { it.toPhotoList() }
    }
}
