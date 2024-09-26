package ui.state

import androidx.compose.runtime.Stable
import data.model.remote.UnsplashPhotoRemote

@Stable
internal data class PhotoDetailState(
    val isLoading: Boolean = true,
    val photo: UnsplashPhotoRemote? = null,
    val error: Throwable? = null,
    var intentPhotoId: String? = null
)
