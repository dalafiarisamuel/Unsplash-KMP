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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import data.repository.SharedRepository
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import ui.navigation.PhotoScreen
import ui.screen.HomeScreenEntryPoint
import ui.screen.PhotoDetailScreenEntryPoint
import ui.theme.UnsplashKMPTheme
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.select_an_image_from_the_list

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
fun App() {

    val sharedRepository = koinInject<SharedRepository>()
    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(isSystemInDarkTheme()) {
        sharedRepository.isDarkThemeEnabled = isSystemInDarkTheme
    }

    val backStack: MutableList<PhotoScreen> =
        rememberSerializable(serializer = SnapshotStateListSerializer()) {
            mutableStateListOf(PhotoScreen.HomeScreen)
        }

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(
                horizontalPartitionSpacerSize = 0.dp,
            )
    }

    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

    UnsplashKMPTheme(darkTheme = sharedRepository.isDarkThemeEnabled) {

        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            sceneStrategy = listDetailStrategy,
            entryProvider = entryProvider {
                entry<PhotoScreen.HomeScreen>(
                    metadata = ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colors.background)
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = stringResource(Res.string.select_an_image_from_the_list),
                                    color = appWhite,
                                    fontSize = 14.sp,
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
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
                        isDarkTheme = sharedRepository.isDarkThemeEnabled,
                        flipTheme = { sharedRepository.flipTheme() }
                    )
                }

                entry<PhotoScreen.DetailScreen>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { backStackEntry ->
                    PhotoDetailScreenEntryPoint(
                        showNavigationBackIcon = true, //listDetailStrategy.directive.maxHorizontalPartitions == 1,
                        navigateBack = { backStack.removeLastOrNull() },
                        photoId = backStackEntry.id
                    )
                }
            }
        )
    }
}