import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.ARG_PHOTO_ID
import ui.navigation.PhotoScreen
import ui.screen.HomeScreenEntryPoint
import ui.screen.PhotoDetailScreenEntryPoint
import ui.theme.UnsplashKMPTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    UnsplashKMPTheme {

        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = PhotoScreen.PHOTO_LIST.route) {

            composable(PhotoScreen.PHOTO_LIST.route) { HomeScreenEntryPoint(navController) }

            composable(
                route = PhotoScreen.PHOTO_DETAIL.route,
                arguments = listOf(navArgument(ARG_PHOTO_ID) { type = NavType.StringType }),
            ) { backStackEntry ->
                PhotoDetailScreenEntryPoint(
                    navController = navController,
                    photoId = backStackEntry.arguments?.getString(ARG_PHOTO_ID) ?: ""
                )
            }

        }
    }
}