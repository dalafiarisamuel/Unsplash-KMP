package ui.navigation

import kotlinx.serialization.Serializable

internal sealed interface PhotoScreen {
    @Serializable
    data object HomeScreen : PhotoScreen

    @Serializable
    data class DetailScreen(val id: String) : PhotoScreen
}
