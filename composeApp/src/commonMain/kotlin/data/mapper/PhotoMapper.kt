package data.mapper

import data.model.remote.UnsplashPhotoRemote
import data.model.ui.Photo

internal class PhotoMapper constructor(
    private val photosUrlsMapper: PhotosUrlsMapper,
    private val photoCreatorMapper: PhotoCreatorMapper
) : UIModelMapper<UnsplashPhotoRemote, Photo>() {
    override fun mapToUI(entity: UnsplashPhotoRemote): Photo {
        return with(entity) {
            Photo(
                id,
                blurHash,
                width,
                height,
                color,
                alternateDescription,
                description,
                photosUrlsMapper.mapToUI(urls),
                photoCreatorMapper.mapToUI(user)
            )
        }
    }

    override fun mapFromUI(model: Photo): UnsplashPhotoRemote {
        throw Exception("Doesn't work that way")
    }
}