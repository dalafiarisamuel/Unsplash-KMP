package ui.navigation

import androidx.navigation3.runtime.NavKey

internal object BookmarkMatcher : DeeplinkMatcher {
    private val bookmarkRegex = """unsplashkmp://bookmarks""".toRegex()

    override fun match(deeplink: String): NavKey? {
        val match = bookmarkRegex.find(deeplink)
        return match?.let {
            PhotoScreen.BookmarkScreen
        }
    }
}
