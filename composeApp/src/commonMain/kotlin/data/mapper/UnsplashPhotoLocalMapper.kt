package data.mapper

import data.local.model.ProfileImageLocal
import data.local.model.UnsplashPhotoLocal
import data.local.model.UnsplashPhotoUrlsLocal
import data.local.model.UnsplashUserLocal
import data.remote.model.ProfileImage
import data.remote.model.UnsplashPhotoRemote
import data.remote.model.UnsplashPhotoUrls
import data.remote.model.UnsplashUser
import data.ui.model.Photo
import data.ui.model.PhotoCreator
import data.ui.model.PhotoUrls

fun List<UnsplashPhotoLocal>.toPhotoList(): List<Photo> {
    return this.map { it.toPhoto() }
}

fun List<UnsplashPhotoRemote>.toUnsplashPhotoLocalList(): List<UnsplashPhotoLocal> {
    return this.map { it.toUnsplashPhotoLocal() }
}

fun UnsplashPhotoLocal.toPhoto(): Photo {
    return Photo(
        id = id,
        blurHash = blurHash,
        width = width,
        height = height,
        color = color,
        alternateDescription = alternateDescription,
        description = description,
        urls = urls.toPhotoUrls(),
        user = user.toPhotoCreator(),
    )
}

private fun UnsplashPhotoUrlsLocal.toPhotoUrls(): PhotoUrls {
    return PhotoUrls(raw = raw, full = full, regular = regular, small = small, thumb = thumb)
}

private fun UnsplashUserLocal.toPhotoCreator(): PhotoCreator {
    return PhotoCreator(name = name, username = username, attributionUrl = attributionUrl)
}

fun UnsplashPhotoRemote.toUnsplashPhotoLocal(): UnsplashPhotoLocal {
    return UnsplashPhotoLocal(
        id = id,
        blurHash = blurHash,
        width = width,
        height = height,
        color = color,
        likes = likes,
        alternateDescription = alternateDescription,
        createdAt = createdAt,
        description = description,
        urls = urls.toUnsplashPhotoUrlsLocal(),
        user = user.toUnsplashUserLocal(),
    )
}

private fun UnsplashPhotoUrls.toUnsplashPhotoUrlsLocal(): UnsplashPhotoUrlsLocal {
    return UnsplashPhotoUrlsLocal(
        raw = raw,
        full = full,
        regular = regular,
        small = small,
        thumb = thumb,
    )
}

private fun UnsplashUser.toUnsplashUserLocal(): UnsplashUserLocal {
    return UnsplashUserLocal(
        id = id,
        name = name,
        username = username,
        bio = bio,
        totalCollections = totalCollections,
        totalPhotos = totalPhotos,
        totalLikes = totalLikes,
        portfolioUrl = portfolioUrl,
        profileImage = profileImage.toProfileImageLocal(),
    )
}

private fun ProfileImage.toProfileImageLocal(): ProfileImageLocal {
    return ProfileImageLocal(small = small, medium = medium, large = large)
}
