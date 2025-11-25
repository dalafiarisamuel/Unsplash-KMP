package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import ui.theme.appWhite
import ui.viewmodel.HomeScreenViewModel

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
internal fun HomeScreenEntryPoint(
    navigateToDetailScreen: (imageId: String) -> Unit = {},
    flipTheme: () -> Unit = {},
    isDarkTheme: Boolean,
    viewModel: HomeScreenViewModel = koinViewModel<HomeScreenViewModel>(),
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(50.dp),
                onClick = flipTheme
            ) {
                val icon = if (isDarkTheme) Icons.Rounded.LightMode
                else Icons.Rounded.DarkMode

                Icon(
                    imageVector = icon,
                    tint = appWhite,
                    contentDescription = null,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        HomeScreen(
            state = viewModel.state,
            imageList = viewModel.photos,
            dispatch = viewModel::dispatch
        ) { image ->
            navigateToDetailScreen(image.id)
        }
    }
}
