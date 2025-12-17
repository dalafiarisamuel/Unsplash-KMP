package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.ImageRepository
import data.repository.Resource
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
) : ViewModel(), KoinComponent {


    val uiState: StateFlow<PhotoDetailState>
        field = MutableStateFlow<PhotoDetailState>(PhotoDetailState())

    val imageDownloadState: StateFlow<ImageDownloadState>
        field = MutableStateFlow<ImageDownloadState>(ImageDownloadState.Idle)


    val isDownloading: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)


    init {
        handleLoadingState()
    }

    private fun handleLoadingState() {
        imageDownloadState.map {
            it is ImageDownloadState.Loading
        }.onEach {
            isDownloading.emit(it)
        }.launchIn(viewModelScope)
    }

    fun getSelectedPhotoById(photoId: String) {
        uiState.update { it.copy(intentPhotoId = photoId) }
        viewModelScope.launch {
            when (val result = repository.getPhoto(photoId = photoId)) {
                is Resource.Success -> {
                    uiState.update {
                        it.copy(
                            isLoading = false,
                            photo = result.result,
                            error = null
                        )
                    }
                }

                is Resource.Failure -> {
                    uiState.update {
                        it.copy(
                            isLoading = false,
                            photo = null,
                            error = result.error
                        )
                    }
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