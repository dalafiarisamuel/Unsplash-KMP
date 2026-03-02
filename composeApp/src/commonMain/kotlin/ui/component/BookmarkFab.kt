package ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.ColorCrimsonRed
import ui.theme.appWhite

@Composable
fun BookmarkFab(count: Int, navigateToBookmarks: () -> Unit) {
    BadgedBox(
        badge = {
            if (count > 0) {
                Badge(backgroundColor = ColorCrimsonRed) {
                    Text(
                        text = count.formatCount(),
                        style = MaterialTheme.typography.caption.copy(fontSize = 9.sp),
                    )
                }
            }
        }
    ) {
        FloatingActionButton(modifier = Modifier.size(50.dp), onClick = navigateToBookmarks) {
            Icon(imageVector = Icons.Rounded.Bookmarks, tint = appWhite, contentDescription = null)
        }
    }
}

private fun Int.formatCount(): String {
    return if (this > 99) "99+" else "$this"
}
