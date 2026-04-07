@file:OptIn(ExperimentalSharedTransitionApi::class)

package ui.screen

import Platform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import getPlatform
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import ui.dialog.DownloadResultDialog
import ui.navigation.runWithPermission
import ui.state.ImageDownloadState
import ui.theme.appDark
import ui.viewmodel.PhotoDetailViewModel
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.done
import unsplashkmp.composeapp.generated.resources.download_complete_message
import unsplashkmp.composeapp.generated.resources.download_complete_title
import unsplashkmp.composeapp.generated.resources.download_failed_message
import unsplashkmp.composeapp.generated.resources.download_failed_title
import unsplashkmp.composeapp.generated.resources.okay

@OptIn(KoinExperimentalAPI::class, ExperimentalPermissionsApi::class)
@Composable
internal fun PhotoDetailScreenEntryPoint(
    navigateBack: () -> Unit,
    showNavigationBackIcon: Boolean = true,
    photoId: String,
    viewModel: PhotoDetailViewModel = koinViewModel<PhotoDetailViewModel> { parametersOf(photoId) },
) {

    val writeStorage = rememberPermissionState(Permission.WriteStorage)

    val gallery = rememberPermissionState(Permission.Gallery)

    val currentPlatform = remember { getPlatform() }

    val showDialog = viewModel.isDownloading.collectAsStateWithLifecycle().value

    val downloadState = viewModel.imageDownloadState.collectAsStateWithLifecycle().value

    PhotoDetail(
        modifier = Modifier.fillMaxSize(),
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        showNavigationBackIcon = showNavigationBackIcon,
        onBackPressed = navigateBack,
        onBookmarkClicked = viewModel::saveOrRemovePhotoFromFavourite,
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
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        ) {
            Column(
                modifier = Modifier.size(100.dp).background(appDark, RoundedCornerShape(10.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(strokeWidth = 2.dp)
            }
        }
    }

    when (downloadState) {
        is ImageDownloadState.Success ->
            DownloadResultDialog(
                title = stringResource(Res.string.download_complete_title),
                message = stringResource(Res.string.download_complete_message),
                buttonLabel = stringResource(Res.string.done),
                onDismiss = viewModel::resetDownloadState,
            )
        is ImageDownloadState.Failure ->
            DownloadResultDialog(
                title = stringResource(Res.string.download_failed_title),
                message =
                    downloadState.exception.message
                        ?: stringResource(Res.string.download_failed_message),
                buttonLabel = stringResource(Res.string.okay),
                onDismiss = viewModel::resetDownloadState,
            )
        else -> Unit
    }
}
