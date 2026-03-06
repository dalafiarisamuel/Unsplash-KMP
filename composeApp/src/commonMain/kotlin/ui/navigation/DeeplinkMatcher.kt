package ui.navigation

import androidx.navigation3.runtime.NavKey

internal interface DeeplinkMatcher {
    fun match(deeplink: String): NavKey?
}
