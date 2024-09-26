package ui.download

import ui.state.ImageDownloadState

internal expect class PlatformDownloadImage {
    suspend fun downloadImage(imageLink: String): ImageDownloadState
}