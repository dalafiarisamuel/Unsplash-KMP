package ui.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.ImageRepository
import data.repository.Resource
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ui.download.PlatformDownloadImage
import ui.state.ImageDownloadState
import ui.state.PhotoDetailState


internal class PhotoDetailViewModel(
    private val repository: ImageRepository,
    private val platformDownloadImage: PlatformDownloadImage,
) : ViewModel(), KoinComponent {

    var uiState by mutableStateOf(PhotoDetailState())

    var imageDownloadState by mutableStateOf<ImageDownloadState>(ImageDownloadState.Idle)

    val isDownloading by derivedStateOf { imageDownloadState is ImageDownloadState.Loading }

    fun getSelectedPhotoById(photoId: String) {
        uiState.intentPhotoId = photoId

        viewModelScope.launch {

            when (val result = repository.getPhoto(photoId = photoId)) {
                is Resource.Success -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        photo = result.result,
                        error = null
                    )
                }

                is Resource.Failure -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        photo = null,
                        error = result.error
                    )
                }
            }
        }
    }

    fun startDownload(photoUrl: String) {
        viewModelScope.launch {
            imageDownloadState = ImageDownloadState.Loading
            imageDownloadState = platformDownloadImage.downloadImage(photoUrl)
        }
    }
}