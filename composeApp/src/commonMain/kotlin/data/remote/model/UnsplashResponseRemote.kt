package data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UnsplashResponseRemote(
    val total: Long,
    @SerialName( "total_pages")
    val totalPages: Long,
    val results: List<UnsplashPhotoRemote>
)