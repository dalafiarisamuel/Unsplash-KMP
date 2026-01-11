package ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface PhotoScreen: NavKey {
    @Serializable
    data object HomeScreen : PhotoScreen

    @Serializable
    data class DetailScreen(val id: String) : PhotoScreen

    @Serializable
    data object BookmarkScreen : PhotoScreen

    @Serializable
    data class BookmarkDetailScreen(val id: String) : PhotoScreen
}
