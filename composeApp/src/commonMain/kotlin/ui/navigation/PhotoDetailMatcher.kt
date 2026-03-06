package ui.navigation

import androidx.navigation3.runtime.NavKey

internal object PhotoDetailMatcher : DeeplinkMatcher {
    private val photoDetailRegex = """unsplashkmp://photo\?id=(.*)""".toRegex()

    override fun match(deeplink: String): NavKey? {
        val match = photoDetailRegex.find(deeplink)
        return match?.let {
            PhotoScreen.DetailScreen(it.groupValues[1])
        }
    }
}
