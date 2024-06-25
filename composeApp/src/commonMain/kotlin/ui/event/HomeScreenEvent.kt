package ui.event

import data.model.ui.Photo


sealed class HomeScreenEvent {

    data class SelectChip(val chipValue: String) : HomeScreenEvent()

    data class SelectImage(val image: Photo?) : HomeScreenEvent()

    data class OnImageClicked(val image: Photo?) : HomeScreenEvent()

    data class UpdateSearchField(val searchTerm: String) : HomeScreenEvent()

    sealed class ImagePreviewDialog : HomeScreenEvent() {

        data object Dismiss : ImagePreviewDialog()

        data object Open : ImagePreviewDialog()
    }

    data object Search : HomeScreenEvent()

}
