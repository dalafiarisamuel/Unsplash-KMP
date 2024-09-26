package ui.screen

import Platform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import getPlatform
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.navigation.runWithPermission
import ui.theme.appDark
import ui.viewmodel.PhotoDetailViewModel

@OptIn(KoinExperimentalAPI::class, ExperimentalPermissionsApi::class)
@Composable
internal fun PhotoDetailScreenEntryPoint(
    navController: NavController,
    photoId: String,
    viewModel: PhotoDetailViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getSelectedPhotoById(photoId)
    }

    val writeStorage = rememberPermissionState(
        Permission.WriteStorage
    )

    val gallery = rememberPermissionState(
        Permission.Gallery
    )

    val currentPlatform = remember { getPlatform() }

    var showDialog = viewModel.isDownloading

    PhotoDetail(
        modifier = Modifier.fillMaxSize(),
        state = viewModel.uiState,
        onBackPressed = { navController.popBackStack() }
    ) {
        when (currentPlatform) {
            Platform.Android -> {
                runWithPermission(writeStorage) {
                    if (it != null) {
                        viewModel.startDownload(it)
                    }
                }
            }

            Platform.Ios -> {
                runWithPermission(gallery) {
                    if (it != null) {
                        viewModel.startDownload(it)
                    }
                }
            }

            Platform.Desktop -> {
                if (it != null) {
                    viewModel.startDownload(it)
                }
            }
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Column(
                modifier = Modifier.size(100.dp).background(appDark, RoundedCornerShape(10.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(strokeWidth = 2.dp)
            }
        }
    }
}