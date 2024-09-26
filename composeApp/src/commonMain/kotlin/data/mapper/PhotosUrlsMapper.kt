package data.mapper

import data.model.remote.UnsplashPhotoUrls
import data.model.ui.PhotoUrls

internal class PhotosUrlsMapper : UIModelMapper<UnsplashPhotoUrls, PhotoUrls>() {
    override fun mapToUI(entity: UnsplashPhotoUrls): PhotoUrls {
        return with(entity) {
            PhotoUrls(raw, full, regular, small, thumb)
        }
    }

    override fun mapFromUI(model: PhotoUrls): UnsplashPhotoUrls {
        throw Exception("Doesn't work that way")
    }
}