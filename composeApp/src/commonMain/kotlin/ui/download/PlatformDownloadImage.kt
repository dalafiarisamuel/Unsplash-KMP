package ui.download

import ui.state.ImageDownloadState

expect class PlatformDownloadImage {
    suspend fun downloadImage(imageLink: String): ImageDownloadState
}