package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.navigation.PhotoScreen
import ui.viewmodel.HomeScreenViewModel

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    KoinExperimentalAPI::class
)
@Composable
fun HomeScreenEntryPoint(
    navController: NavController,
    viewModel: HomeScreenViewModel = koinViewModel(),
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