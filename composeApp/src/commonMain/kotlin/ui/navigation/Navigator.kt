package ui.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class Navigator(
    private val deeplinkResolver: DeeplinkResolver
) {
    private val _navigationEvents = MutableStateFlow<PhotoScreen?>(null)
    val navigationEvents = _navigationEvents.asStateFlow()

    fun navigateTo(screen: PhotoScreen) {
        _navigationEvents.value = screen
    }

    fun handleDeeplink(deeplink: String) {
        val screen = deeplinkResolver.resolve(deeplink)
        if (screen is PhotoScreen) {
            _navigationEvents.value = screen
        }
    }

    fun onNavigationHandled() {
        _navigationEvents.value = null
    }
}
