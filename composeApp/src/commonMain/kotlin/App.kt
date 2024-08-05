import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
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

        NavHost(navController = navController, startDestination = PhotoScreen.HomeScreen) {

            composable<PhotoScreen.HomeScreen> { HomeScreenEntryPoint(navController) }

            composable<PhotoScreen.DetailScreen> { backStackEntry ->
                val id = backStackEntry.toRoute<PhotoScreen.DetailScreen>().id
                PhotoDetailScreenEntryPoint(
                    navController = navController,
                    photoId = id
                )
            }
        }
    }
}