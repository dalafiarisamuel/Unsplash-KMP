package ui.navigation

import ng.devtamuno.unsplash.compose.ui.navigation.argumentCount
import ng.devtamuno.unsplash.compose.ui.navigation.arguments

const val ARG_PHOTO_ID = "photo_id"

enum class PhotoScreen(val route: String) {
    PHOTO_LIST("home_screen"),
    PHOTO_DETAIL("photo_detail_screen/{$ARG_PHOTO_ID}/detail");

    fun createPath(vararg args: Any): String {
        var route = route
        require(args.size == route.argumentCount) {
            "Provided ${args.count()} parameters, was expected ${route.argumentCount} parameters!"
        }
        route.arguments().forEachIndexed { index, matchResult ->
            route = route.replace(matchResult.value, args[index].toString())
        }
        return route
    }
}
