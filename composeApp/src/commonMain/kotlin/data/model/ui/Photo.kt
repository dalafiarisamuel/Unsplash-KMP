package data.model.ui

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


val dummyPhoto = Photo(
    id = "ldLty8YYYP4",
    color = "#0c2640",
    alternateDescription = "blue sky with stars during night time",
    blurHash = "L02~\$RkEQ*ogH;axogaxn3ofbHay",
    description = "Old stone background texture",
    height = 3238,
    width = 4857,
    urls = PhotoUrls(
        raw = "https://images.unsplash.com/photo-1606707764561-ed73aab9fdd5?ixid=MnwyNDI1MTl8MHwxfHNlYXJjaHw1MXx8Q29tZXR8ZW58MHx8fHwxNjc2NTY0NzQ3&ixlib=rb-4.0.3",
        full = "https://images.unsplash.com/photo-1606707764561-ed73aab9fdd5?crop=entropy&cs=tinysrgb&fm=jpg&ixid=MnwyNDI1MTl8MHwxfHNlYXJjaHw1MXx8Q29tZXR8ZW58MHx8fHwxNjc2NTY0NzQ3&ixlib=rb-4.0.3&q=80",
        regular = "https://images.unsplash.com/photo-1606707764561-ed73aab9fdd5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyNDI1MTl8MHwxfHNlYXJjaHw1MXx8Q29tZXR8ZW58MHx8fHwxNjc2NTY0NzQ3&ixlib=rb-4.0.3&q=80&w=1080",
        small = "https://images.unsplash.com/photo-1606707764561-ed73aab9fdd5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyNDI1MTl8MHwxfHNlYXJjaHw1MXx8Q29tZXR8ZW58MHx8fHwxNjc2NTY0NzQ3&ixlib=rb-4.0.3&q=80&w=400",
        thumb = "https://images.unsplash.com/photo-1606707764561-ed73aab9fdd5?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyNDI1MTl8MHwxfHNlYXJjaHw1MXx8Q29tZXR8ZW58MHx8fHwxNjc2NTY0NzQ3&ixlib=rb-4.0.3&q=80&w=200"
    ),
    user = PhotoCreator(
        name = "Rich Wooten", username = "whatsawoot", attributionUrl = ""
    )
)
