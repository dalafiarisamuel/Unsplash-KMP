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
import androidx.navigation.NavController
import data.repository.SharedRepository
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.navigation.PhotoScreen
import ui.theme.appWhite
import ui.viewmodel.HomeScreenViewModel

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    KoinExperimentalAPI::class
)
@Composable
internal fun HomeScreenEntryPoint(
    navController: NavController,
    viewModel: HomeScreenViewModel = koinViewModel(),
    sharedRepository: SharedRepository = koinInject<SharedRepository>()
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(50.dp),
                onClick = {
                    sharedRepository.isDarkThemeEnabled = sharedRepository.isDarkThemeEnabled.not()
                },
            ) {
                Icon(
                    if (sharedRepository.isDarkThemeEnabled) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
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
            onLongClicked = {
                navController.navigate(PhotoScreen.DetailScreen(it.id))
            },
            dispatch = viewModel::dispatch
        )
    }
}
