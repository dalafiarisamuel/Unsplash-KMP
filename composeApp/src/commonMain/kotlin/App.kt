import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import data.ui.model.Theme
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import ui.navigation.Navigator
import ui.navigation.PhotoScreen
import ui.navigation.SceneKeys
import ui.screen.BookmarkScreenEntryPoint
import ui.screen.HomeScreenEntryPoint
import ui.screen.PhotoDetailScreenEntryPoint
import ui.theme.UnsplashKMPTheme
import ui.theme.appWhite
import ui.viewmodel.PreferenceViewModel
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.select_an_image_from_the_list

private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(PhotoScreen.HomeScreen::class, PhotoScreen.HomeScreen.serializer())
            subclass(PhotoScreen.DetailScreen::class, PhotoScreen.DetailScreen.serializer())
            subclass(PhotoScreen.BookmarkScreen::class, PhotoScreen.BookmarkScreen.serializer())
            subclass(
                PhotoScreen.BookmarkDetailScreen::class,
                PhotoScreen.BookmarkDetailScreen.serializer(),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
internal fun App(
    navigator: Navigator = koinInject<Navigator>(),
    preferenceViewModel: PreferenceViewModel = koinInject<PreferenceViewModel>(),
) {

    val selectedTheme by preferenceViewModel.theme.collectAsStateWithLifecycle()
    val isDarkTheme =
        when (selectedTheme) {
            Theme.SYSTEM -> isSystemInDarkTheme()
            Theme.LIGHT -> false
            Theme.DARK -> true
        }
    val navEvent by navigator.navigationEvents.collectAsStateWithLifecycle()

    val backStack = rememberNavBackStack(config, PhotoScreen.HomeScreen)

    LaunchedEffect(navEvent) {
        navEvent?.let { screen ->
            handleNavigation(backStack, screen)
            navigator.onNavigationHandled()
        }
    }

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive =
        remember(windowAdaptiveInfo) {
            calculatePaneScaffoldDirective(windowAdaptiveInfo)
                .copy(horizontalPartitionSpacerSize = 0.dp)
        }

    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

    UnsplashKMPTheme(darkTheme = isDarkTheme) {
        NavDisplay(
            backStack = backStack,
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            sceneStrategy = listDetailStrategy,
            entryProvider =
                entryProvider {
                    entry<PhotoScreen.HomeScreen>(
                        metadata =
                            ListDetailSceneStrategy.listPane(
                                sceneKey = SceneKeys.SCENE_HOME,
                                detailPlaceholder = {
                                    Box(
                                        modifier =
                                            Modifier.background(MaterialTheme.colors.background)
                                                .fillMaxSize(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            modifier = Modifier.align(Alignment.Center),
                                            text =
                                                stringResource(
                                                    Res.string.select_an_image_from_the_list
                                                ),
                                            color = appWhite,
                                            fontSize = 14.sp,
                                            fontStyle = FontStyle.Normal,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                    }
                                },
                            )
                    ) { _ ->
                        HomeScreenEntryPoint(
                            navigateToDetailScreen = { id ->
                                handleNavigation(backStack, PhotoScreen.DetailScreen(id))
                            },
                            resetSearchInput = {
                                val lastItem = backStack.lastOrNull()
                                if (lastItem is PhotoScreen.DetailScreen) {
                                    backStack.removeLastOrNull()
                                }
                            },
                            theme = selectedTheme,
                            setTheme = { theme -> preferenceViewModel.setTheme(theme) },
                            navigateToBookmarks = {
                                handleNavigation(backStack, PhotoScreen.BookmarkScreen)
                            },
                        )
                    }

                    entry<PhotoScreen.DetailScreen>(
                        metadata =
                            ListDetailSceneStrategy.detailPane(sceneKey = SceneKeys.SCENE_HOME)
                    ) { backStackEntry ->
                        PhotoDetailScreenEntryPoint(
                            showNavigationBackIcon =
                                true, // listDetailStrategy.directive.maxHorizontalPartitions == 1,
                            navigateBack = { backStack.removeLastOrNull() },
                            photoId = backStackEntry.id,
                        )
                    }

                    entry<PhotoScreen.BookmarkScreen>(
                        metadata =
                            ListDetailSceneStrategy.listPane(sceneKey = SceneKeys.SCENE_BOOKMARKS)
                    ) {
                        BookmarkScreenEntryPoint(
                            onBackPressed = { backStack.removeLastOrNull() },
                            onItemClicked = {
                                handleNavigation(backStack, PhotoScreen.BookmarkDetailScreen(it.id))
                            },
                        )
                    }

                    entry<PhotoScreen.BookmarkDetailScreen>(
                        metadata =
                            ListDetailSceneStrategy.detailPane(sceneKey = SceneKeys.SCENE_BOOKMARKS)
                    ) { backStackEntry ->
                        PhotoDetailScreenEntryPoint(
                            showNavigationBackIcon = true,
                            navigateBack = { backStack.removeLastOrNull() },
                            photoId = backStackEntry.id,
                        )
                    }
                },
        )
    }
}

private fun handleNavigation(backStack: NavBackStack<NavKey>, screen: PhotoScreen) {
    val lastItem = backStack.lastOrNull()
    if (lastItem is PhotoScreen.DetailScreen && screen is PhotoScreen.DetailScreen) {
        backStack[backStack.lastIndex] = screen
    } else if (
        lastItem is PhotoScreen.BookmarkDetailScreen && screen is PhotoScreen.BookmarkDetailScreen
    ) {
        backStack[backStack.lastIndex] = screen
    } else if (lastItem != screen) {
        backStack.add(screen)
    }
}
