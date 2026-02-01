package ui.event

import data.ui.model.Photo

internal sealed class HomeScreenEvent {

    data class SelectChip(val chipValue: String) : HomeScreenEvent()

    data class SelectImage(val image: Photo?) : HomeScreenEvent()

    data class OnImageLongClicked(val image: Photo?) : HomeScreenEvent()

    data class UpdateSearchField(val searchTerm: String) : HomeScreenEvent()

    sealed class ImagePreviewDialog : HomeScreenEvent() {

        object Dismiss : ImagePreviewDialog()

        object Open : ImagePreviewDialog()
    }

    object Search : HomeScreenEvent()

    sealed class SearchQueryChip : HomeScreenEvent() {

        object ClearAll : SearchQueryChip()

        data class Delete(val query: String) : SearchQueryChip()

        data class Save(val query: String) : SearchQueryChip()

        object OpenSaveQueryDialog : SearchQueryChip()

        object DismissSaveQueryDialog: SearchQueryChip()
    }
}
