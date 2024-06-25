package ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.ImageRepository
import data.repository.Resource
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import ui.state.PhotoDetailState


class PhotoDetailViewModel(
    private val repository: ImageRepository,
) : ViewModel(), KoinComponent {

    var uiState by mutableStateOf(PhotoDetailState())

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
                        error = result.error
                    )
                }
            }
        }
    }

}