import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
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
fun App(preferenceViewModel: PreferenceViewModel = koinInject<PreferenceViewModel>()) {

    val isDarkTheme by preferenceViewModel.isDarkMode.collectAsStateWithLifecycle()

    val backStack = rememberNavBackStack(config, PhotoScreen.HomeScreen)

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
                    ) {
                        HomeScreenEntryPoint(
                            navigateToDetailScreen = {
                                val lastItem = backStack.lastOrNull()
                                if (lastItem is PhotoScreen.DetailScreen) {
                                    backStack[backStack.lastIndex] = PhotoScreen.DetailScreen(it)
                                } else {
                                    backStack.add(PhotoScreen.DetailScreen(it))
                                }
                            },
                            resetSearchInput = {
                                val lastItem = backStack.lastOrNull()
                                if (lastItem is PhotoScreen.DetailScreen) {
                                    backStack.removeLastOrNull()
                                }
                            },
                            isDarkTheme = isDarkTheme,
                            flipTheme = { preferenceViewModel.toggleTheme(isDarkTheme.not()) },
                            navigateToBookmarks = { backStack.add(PhotoScreen.BookmarkScreen) },
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
                                val lastItem = backStack.lastOrNull()
                                if (lastItem is PhotoScreen.BookmarkDetailScreen) {
                                    backStack[backStack.lastIndex] =
                                        PhotoScreen.BookmarkDetailScreen(it.id)
                                } else {
                                    backStack.add(PhotoScreen.BookmarkDetailScreen(it.id))
                                }
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
