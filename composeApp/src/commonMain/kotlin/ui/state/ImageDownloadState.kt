package ui.state

sealed class ImageDownloadState {
    data object Idle : ImageDownloadState()
    data object Loading : ImageDownloadState()
    data object Successful : ImageDownloadState()
    data class Failure(val exception: Throwable) : ImageDownloadState()
}