package ui.navigation

import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.PermissionState
import com.mohamedrejeb.calf.permissions.PermissionStatus

@OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)
fun runWithPermission(permissionState: PermissionState, ifGranted: () -> Unit) {
    when (val status = permissionState.status) {
        is PermissionStatus.Denied -> {
            if (status.shouldShowRationale) {
                permissionState.launchPermissionRequest()
            } else {
                permissionState.openAppSettings()
            }
        }

        is PermissionStatus.Granted -> {
            ifGranted()
        }
    }
}