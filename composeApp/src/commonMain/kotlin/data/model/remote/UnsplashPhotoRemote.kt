package data.model.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
internal data class UnsplashPhotoRemote(
    val id: String,
    @SerialName("blur_hash")
    val blurHash: String?,
    val width: Int,
    val height: Int,
    val color: String,
    val likes: Int,
    @SerialName("alt_description")
    val alternateDescription: String?,
    @SerialName("created_at")
    val createdAt: String,
    val description: String?,
    val urls: UnsplashPhotoUrls,
    val user: UnsplashUser
)

@Serializable
internal data class UnsplashPhotoUrls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)

@Serializable
internal data class UnsplashUser(
    val id: String,
    val name: String,
    val username: String,
    val bio: String?,
    @SerialName("total_collections")
    val totalCollections: Int,
    @SerialName("total_photos")
    val totalPhotos: Long,
    @SerialName("total_likes")
    val totalLikes: Long,
    @SerialName("portfolio_url")
    val portfolioUrl: String?,
    @SerialName("profile_image")
    val profileImage: ProfileImage,
) {
    val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageLoader&utm_medium=referral"
}

@Serializable
internal data class ProfileImage(
    val small: String,
    val medium: String,
    val large: String
)