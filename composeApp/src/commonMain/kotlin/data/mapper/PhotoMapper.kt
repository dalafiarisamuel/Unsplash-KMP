package data.mapper

import data.remote.model.UnsplashPhotoRemote
import data.ui.model.Photo


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
}