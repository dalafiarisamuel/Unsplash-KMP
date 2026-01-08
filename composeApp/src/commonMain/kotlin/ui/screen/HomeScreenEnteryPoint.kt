package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ui.theme.appWhite
import ui.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun HomeScreenEntryPoint(
    navigateToDetailScreen: (imageId: String) -> Unit = {},
    navigateToBookmarks: () -> Unit = {},
    resetSearchInput: () -> Unit = {},
    flipTheme: () -> Unit = {},
    isDarkTheme: Boolean = false,
    viewModel: HomeScreenViewModel = koinViewModel<HomeScreenViewModel>(),
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.size(50.dp), onClick = navigateToBookmarks) {
                Icon(
                    imageVector = Icons.Rounded.Bookmarks,
                    tint = appWhite,
                    contentDescription = null,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        HomeScreen(
            state = viewModel.state,
            imageList = viewModel.photos,
            dispatch = viewModel::dispatch,
            resetSearchInput = resetSearchInput,
            isDarkTheme = isDarkTheme,
            flipTheme = flipTheme,
        ) { image ->
            navigateToDetailScreen(image.id)
        }
    }
}
