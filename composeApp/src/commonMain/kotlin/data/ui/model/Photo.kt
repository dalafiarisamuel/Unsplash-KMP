package data.ui.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class Photo(
    val id: String,
    val blurHash: String?,
    val width: Int,
    val height: Int,
    val color: String,
    val alternateDescription: String?,
    val description: String?,
    val urls: PhotoUrls,
    val user: PhotoCreator,
)

@Stable
@Serializable
data class PhotoUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
)

@Stable
@Serializable
data class PhotoCreator(
    val name: String,
    val username: String,
    val attributionUrl: String,
)
