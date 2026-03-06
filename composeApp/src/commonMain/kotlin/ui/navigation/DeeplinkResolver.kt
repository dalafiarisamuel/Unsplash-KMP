package ui.navigation

import androidx.navigation3.runtime.NavKey

internal class DeeplinkResolver(
    private val fallbackDestination: NavKey = PhotoScreen.HomeScreen,
    private val matchers: List<DeeplinkMatcher> = listOf(
        PhotoDetailMatcher
    )
) {
    fun resolve(deeplink: String): NavKey {
        for (matcher in matchers) {
            matcher.match(deeplink)?.let { return it }
        }
        return fallbackDestination
    }
}
