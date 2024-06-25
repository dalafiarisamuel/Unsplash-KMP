package data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashResponseRemote(
    val total: Long,
    @SerialName( "total_pages")
    val totalPages: Long,
    val results: List<UnsplashPhotoRemote>
)