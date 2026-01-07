package data.repository

import data.local.dao.UnsplashPhotoDao
import data.local.model.UnsplashPhotoLocal
import data.local.repository.UnsplashImageLocalRepository
import data.remote.repository.Resource
import data.remote.repository.resourceHelper
import kotlinx.coroutines.flow.Flow

class UnsplashImageLocalRepositoryImpl(private val photoDao: UnsplashPhotoDao) :
    UnsplashImageLocalRepository {
    override suspend fun insertPhoto(photoLocal: UnsplashPhotoLocal): Resource<Unit> {
        return resourceHelper { photoDao.insertPhoto(photoLocal) }
    }

    override fun getAllPhotoAsFlow(): Flow<List<UnsplashPhotoLocal>> {
        return photoDao.getAllPhotoAsFlow()
    }

    override suspend fun getPhotoById(id: String): Resource<UnsplashPhotoLocal?> {
        return resourceHelper { photoDao.getPhotoById(id) }
    }

    override suspend fun deletePhoto(photoLocal: UnsplashPhotoLocal): Resource<Int> {
        return resourceHelper { photoDao.deletePhoto(photoLocal.id) }
    }

    override suspend fun clearAllPhotos(): Resource<Unit> {
        return resourceHelper { photoDao.clearAllPhotos() }

    }
}