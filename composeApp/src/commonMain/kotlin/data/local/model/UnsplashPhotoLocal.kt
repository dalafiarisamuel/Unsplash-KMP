package data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock

@Entity
data class UnsplashPhotoLocal(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Long = 0,
    val id: String,
    val blurHash: String?,
    val width: Int,
    val height: Int,
    val color: String,
    val likes: Int,
    val alternateDescription: String?,
    val createdAt: String,
    val description: String?,
    val databaseCreatedDate: Long = Clock.System.now().toEpochMilliseconds(),
    @Embedded(prefix = "urls_")
    val urls: UnsplashPhotoUrlsLocal,
    @Embedded(prefix = "user_")
    val user: UnsplashUserLocal
)


data class UnsplashPhotoUrlsLocal(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)


data class UnsplashUserLocal(
    val id: String,
    val name: String,
    val username: String,
    val bio: String?,
    val totalCollections: Int,
    val totalPhotos: Long,
    val totalLikes: Long,
    val portfolioUrl: String?,
    @Embedded
    val profileImage: ProfileImageLocal,
) {
    val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageLoader&utm_medium=referral"
}


data class ProfileImageLocal(
    val small: String,
    val medium: String,
    val large: String
)
