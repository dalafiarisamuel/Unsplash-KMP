package data.mapper

import data.model.remote.UnsplashUser
import data.model.ui.PhotoCreator

class PhotoCreatorMapper : UIModelMapper<UnsplashUser, PhotoCreator>() {
    override fun mapToUI(entity: UnsplashUser): PhotoCreator {
        return with(entity) {
            PhotoCreator(name, username, attributionUrl)
        }
    }

    override fun mapFromUI(model: PhotoCreator): UnsplashUser {
        throw Exception("Doesn't work that way")
    }
}