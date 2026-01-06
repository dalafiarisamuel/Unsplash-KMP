package data.mapper

import data.remote.model.UnsplashUser
import data.ui.model.PhotoCreator


internal class PhotoCreatorMapper : UIModelMapper<UnsplashUser, PhotoCreator>() {
    override fun mapToUI(entity: UnsplashUser): PhotoCreator {
        return with(entity) {
            PhotoCreator(name, username, attributionUrl)
        }
    }
}