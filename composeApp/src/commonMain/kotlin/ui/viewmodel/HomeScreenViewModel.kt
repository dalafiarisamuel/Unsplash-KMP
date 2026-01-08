package ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import data.mapper.PhotoMapper
import data.remote.repository.ImageRepository
import data.source.ImagePagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import ui.event.HomeScreenEvent
import ui.state.HomeScreenState
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class HomeScreenViewModel(
    private val repository: ImageRepository,
    private val photoMapper: PhotoMapper,
) : MviViewModel<HomeScreenEvent, HomeScreenState>(HomeScreenState()) {

    companion object {
        private val DEFAULT_QUERY =
            listOf("Nigeria", "Cameras", "History", "Architecture", "Animals", "Travel", "Fashion")
    }

    private val randomDefaultQuery
        get() = DEFAULT_QUERY.random()

    private var currentQuery: MutableStateFlow<String> = MutableStateFlow(randomDefaultQuery)

    init {

        handleSelectImage()
        handleSearchEvent()

        handleSelectChipEvent()
        handleUpdateSearchFieldEvent()

        handleOpenImagePreviewDialogEvent()
        handleDismissImagePreviewDialogEvent()

        handleOnImageClicked()
    }

    private fun setSearchTerm(query: String) {
        currentQuery.value = query.ifEmpty { randomDefaultQuery }
    }

    val photos =
        currentQuery
            .debounce(1.5.seconds)
            .flatMapLatest { queryString ->
                state = state.copy(searchFieldValue = queryString)
                getImageSearchResult(queryString)
            }
            .cachedIn(viewModelScope)

    private fun getImageSearchResult(query: String) =
        Pager(
                config =
                    PagingConfig(
                        pageSize = ImagePagingSource.PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = {
                    ImagePagingSource(
                        repository = repository,
                        photoMapper = photoMapper,
                        query = query,
                    )
                },
            )
            .flow

    private fun handleSelectChipEvent() {
        on<HomeScreenEvent.SelectChip> {
            state = state.copy(searchFieldValue = it.chipValue)
            setSearchTerm(state.searchFieldValue)
        }
    }

    private fun handleSelectImage() {
        on<HomeScreenEvent.SelectImage> { state = state.copy(selectedImage = it.image) }
    }

    private fun handleSearchEvent() {
        on<HomeScreenEvent.Search> { setSearchTerm(state.searchFieldValue) }
    }

    private fun handleUpdateSearchFieldEvent() {
        on<HomeScreenEvent.UpdateSearchField> {
            state = state.copy(searchFieldValue = it.searchTerm)
            setSearchTerm(state.searchFieldValue)
        }
    }

    private fun handleDismissImagePreviewDialogEvent() {
        on<HomeScreenEvent.ImagePreviewDialog.Dismiss> {
            state = state.copy(isImagePreviewDialogVisible = false)
        }
    }

    private fun handleOpenImagePreviewDialogEvent() {
        on<HomeScreenEvent.ImagePreviewDialog.Open> {
            state = state.copy(isImagePreviewDialogVisible = true)
        }
    }

    private fun handleOnImageClicked() {
        on<HomeScreenEvent.OnImageLongClicked> {
            state = state.copy(selectedImage = it.image, isImagePreviewDialogVisible = true)
        }
    }
}
