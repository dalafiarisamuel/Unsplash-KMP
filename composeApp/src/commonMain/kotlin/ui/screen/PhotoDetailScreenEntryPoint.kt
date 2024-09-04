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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.PermissionStatus
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import getPlatform
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.theme.appDark
import ui.viewmodel.PhotoDetailViewModel

@OptIn(KoinExperimentalAPI::class, ExperimentalPermissionsApi::class)
@Composable
fun PhotoDetailScreenEntryPoint(
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

    var showDialog = viewModel.isDownloading

    PhotoDetail(
        modifier = Modifier.fillMaxSize(),
        state = viewModel.uiState,
        onBackPressed = { navController.popBackStack() }
    ) {
        when (getPlatform()) {
            Platform.Android -> {

                when (val status = writeStorage.status) {
                    is PermissionStatus.Denied -> {
                        if (status.shouldShowRationale) {
                            writeStorage.launchPermissionRequest()
                        } else {
                            writeStorage.openAppSettings()
                        }
                    }

                    is PermissionStatus.Granted -> {
                        if (it != null) {
                            viewModel.startDownload(it)
                        }
                    }
                }
            }

            Platform.Apple -> {
                when (val status = gallery.status) {
                    is PermissionStatus.Denied -> {
                        if (status.shouldShowRationale) {
                            gallery.launchPermissionRequest()
                        } else {
                            gallery.openAppSettings()
                        }
                    }

                    is PermissionStatus.Granted -> {
                        if (it != null) {
                            viewModel.startDownload(it)
                        }
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
                modifier = Modifier.size(100.dp).background(appDark, RoundedCornerShape(8.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(strokeWidth = 2.dp)
            }
        }
    }
}