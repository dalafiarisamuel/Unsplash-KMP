import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import data.repository.SharedRepository
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import ui.navigation.PhotoScreen
import ui.screen.HomeScreenEntryPoint
import ui.screen.PhotoDetailScreenEntryPoint
import ui.theme.UnsplashKMPTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {

    val sharedRepository = koinInject<SharedRepository>()
    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(isSystemInDarkTheme()) {
        sharedRepository.isDarkThemeEnabled = isSystemInDarkTheme
    }

    UnsplashKMPTheme(darkTheme = sharedRepository.isDarkThemeEnabled) {

        val backStack: MutableList<PhotoScreen> =
            rememberSerializable(serializer = SnapshotStateListSerializer()) {
                mutableStateListOf(PhotoScreen.HomeScreen)
            }

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<PhotoScreen.HomeScreen> {
                    HomeScreenEntryPoint(
                        navigateToDetailScreen = {
                            backStack.add(PhotoScreen.DetailScreen(it))
                        },
                        isDarkTheme = sharedRepository.isDarkThemeEnabled,
                        flipTheme = { sharedRepository.flipTheme() }
                    )
                }

                entry<PhotoScreen.DetailScreen> { backStackEntry ->
                    PhotoDetailScreenEntryPoint(
                        navigateBack = { backStack.removeLastOrNull() },
                        photoId = backStackEntry.id
                    )
                }
            }
        )
    }
}