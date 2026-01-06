package data.mapper

import data.remote.model.UnsplashPhotoUrls
import data.ui.model.PhotoUrls


internal class PhotosUrlsMapper : UIModelMapper<UnsplashPhotoUrls, PhotoUrls>() {
    override fun mapToUI(entity: UnsplashPhotoUrls): PhotoUrls {
        return with(entity) {
            PhotoUrls(raw, full, regular, small, thumb)
        }
    }
}