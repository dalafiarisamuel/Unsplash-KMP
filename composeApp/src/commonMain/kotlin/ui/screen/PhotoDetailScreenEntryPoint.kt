package ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.viewmodel.PhotoDetailViewModel

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PhotoDetailScreenEntryPoint(
    navController: NavController,
    photoId: String,
    viewModel: PhotoDetailViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getSelectedPhotoById(photoId)
    }

    PhotoDetail(
        modifier = Modifier.fillMaxSize(),
        state = viewModel.uiState,
        onBackPressed = {
            navController.popBackStack()
        }
    )
}