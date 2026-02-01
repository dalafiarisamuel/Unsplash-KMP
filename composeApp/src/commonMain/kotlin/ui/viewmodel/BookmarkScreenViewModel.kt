package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import domain.usecase.photo.ClearAllPhotosUseCase
import domain.usecase.photo.GetAllPhotoAsFlowUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class BookmarkScreenViewModel(
    getAllPhotoAsFlowUseCase: GetAllPhotoAsFlowUseCase,
    private val clearAllPhotosUseCase: ClearAllPhotosUseCase,
) : ViewModel() {

    val photos =
        getAllPhotoAsFlowUseCase().map {
            PagingData.from(
                data = it,
                sourceLoadStates =
                    LoadStates(
                        refresh = LoadState.NotLoading(endOfPaginationReached = true),
                        prepend = LoadState.NotLoading(endOfPaginationReached = true),
                        append = LoadState.NotLoading(endOfPaginationReached = true),
                    ),
            )
        }

    fun clearAllPhoto() {
        viewModelScope.launch { clearAllPhotosUseCase() }
    }
}
