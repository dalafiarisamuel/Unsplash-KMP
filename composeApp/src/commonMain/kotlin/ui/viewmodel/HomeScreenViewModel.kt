package ui.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import data.mapper.PhotoMapper
import data.remote.repository.ImageRepository
import data.source.ImagePagingSource
import data.ui.model.ChipData
import domain.usecase.preference.ClearSavedSearchQueryUseCase
import domain.usecase.preference.DeleteSavedSearchQueryUseCase
import domain.usecase.preference.GetSavedSearchQueryUseCase
import domain.usecase.preference.SaveSearchQueryUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ui.event.HomeScreenEvent
import ui.state.HomeScreenState
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
internal class HomeScreenViewModel(
    private val repository: ImageRepository,
    private val photoMapper: PhotoMapper,
    private val getSavedSearchQueryUseCase: GetSavedSearchQueryUseCase,
    private val saveSearchQueryUseCase: SaveSearchQueryUseCase,
    private val deleteSavedSearchQueryUseCase: DeleteSavedSearchQueryUseCase,
    private val clearSavedSearchQueryUseCase: ClearSavedSearchQueryUseCase,
) : MviViewModel<HomeScreenEvent, HomeScreenState>(HomeScreenState()) {

    companion object {
        private val DEFAULT_QUERY =
            listOf("Nigeria", "Cameras", "History", "Architecture", "Bronze", "Travel", "Fashion")
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

        handleOpenSaveSearchQueryDialogEvent()
        handleClearAllSavedSearchQuery()
        handleDeleteSavedSearchQuery()
        handleSaveSearchQuery()
        handleDismissSaveSearchQueryDialogEvent()
    }

    private fun setSearchTerm(query: String) {
        currentQuery.value = query.ifEmpty { randomDefaultQuery }
    }

    val photos =
        currentQuery
            .debounce(2.seconds)
            .flatMapLatest { queryString ->
                state = state.copy(searchFieldValue = queryString)
                getImageSearchResult(queryString)
            }
            .cachedIn(viewModelScope)

    val savedSearchQuery =
        getSavedSearchQueryUseCase()
            .map { it.map { item -> ChipData("🤖", item) } }
            .stateInUi(emptyList())

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

    private fun handleSaveSearchQuery() {
        on<HomeScreenEvent.SearchQueryChip.Save> {
            viewModelScope.launch { saveSearchQueryUseCase(it.query) }
        }
    }

    private fun handleDeleteSavedSearchQuery() {
        on<HomeScreenEvent.SearchQueryChip.Delete> {
            viewModelScope.launch { deleteSavedSearchQueryUseCase(it.query) }
        }
    }

    private fun handleClearAllSavedSearchQuery() {
        on<HomeScreenEvent.SearchQueryChip.ClearAll> {
            viewModelScope.launch { clearSavedSearchQueryUseCase() }
        }
    }

    private fun handleOpenSaveSearchQueryDialogEvent() {
        on<HomeScreenEvent.SearchQueryChip.OpenSaveQueryDialog> {
            state = state.copy(isSaveQueryDialogVisible = true)
        }
    }

    private fun handleDismissSaveSearchQueryDialogEvent() {
        on<HomeScreenEvent.SearchQueryChip.DismissSaveQueryDialog> {
            state = state.copy(isSaveQueryDialogVisible = false)
        }
    }
}
