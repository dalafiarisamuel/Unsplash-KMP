package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.remote.model.UnsplashPhotoRemote
import data.remote.repository.ImageRepository
import data.remote.repository.Resource
import domain.usecase.photo.DeletePhotoUseCase
import domain.usecase.photo.GetPhotoByIdUseCase
import domain.usecase.photo.SavePhotoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ui.download.PlatformDownloadImage
import ui.state.ImageDownloadState
import ui.state.PhotoDetailState

internal class PhotoDetailViewModel(
    private val repository: ImageRepository,
    private val platformDownloadImage: PlatformDownloadImage,
    private val savePhotoUseCase: SavePhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val getPhotoByIdUseCase: GetPhotoByIdUseCase,
    photoId: String,
) : ViewModel(), KoinComponent {

    val uiState: StateFlow<PhotoDetailState>
        field = MutableStateFlow<PhotoDetailState>(PhotoDetailState())

    val imageDownloadState: StateFlow<ImageDownloadState>
        field = MutableStateFlow<ImageDownloadState>(ImageDownloadState.Idle)

    val isDownloading: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    init {
        handleLoadingState()
        getSelectedPhotoById(photoId)
    }

    private fun handleLoadingState() {
        imageDownloadState
            .map { it is ImageDownloadState.Loading }
            .onEach { isDownloading.emit(it) }
            .launchIn(viewModelScope)
    }

    private fun getSelectedPhotoById(photoId: String) {
        uiState.update { it.copy(intentPhotoId = photoId) }
        viewModelScope.launch {
            when (val result = repository.getPhoto(photoId = photoId)) {
                is Resource.Success -> {
                    val photoFromCache = getPhotoByIdUseCase(photoId)
                    val isFavourite = photoFromCache is Resource.Success
                    uiState.update {
                        it.copy(
                            isLoading = false,
                            photo = result.result,
                            error = null,
                            isImageFavourite = isFavourite,
                        )
                    }
                }

                is Resource.Failure -> {
                    uiState.update {
                        it.copy(isLoading = false, photo = null, error = result.error)
                    }
                }
            }
        }
    }

    fun saveOrRemovePhotoFromFavourite(photo: UnsplashPhotoRemote) {
        viewModelScope.launch {
            if (uiState.value.isImageFavourite) {
                val result = deletePhotoUseCase(photo)
                if (result is Resource.Success && result.result > 0) {
                    uiState.update { it.copy(isImageFavourite = false) }
                }
            } else {
                val result = savePhotoUseCase(photo)
                if (result is Resource.Success) {
                    uiState.update { it.copy(isImageFavourite = true) }
                }
            }
        }
    }

    fun startDownload(photoUrl: String) {
        viewModelScope.launch {
            imageDownloadState.emit(ImageDownloadState.Loading)
            imageDownloadState.emit(platformDownloadImage.downloadImage(photoUrl))
        }
    }
}
