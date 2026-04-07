package ui.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import ui.theme.appDark
import ui.theme.appWhite


@Composable
fun DownloadResultDialog(
    title: String,
    message: String,
    buttonLabel: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        backgroundColor = appDark,
        contentColor = appWhite,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(buttonLabel) }
        },
    )
}