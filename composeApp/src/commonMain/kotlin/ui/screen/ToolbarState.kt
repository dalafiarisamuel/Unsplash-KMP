package ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

data class ToolbarState(
    val height: Dp,
    val offset: Float,
    val nestedScrollConnection: NestedScrollConnection,
    val reset: () -> Unit,
)


@Composable
fun rememberToolbarState(toolbarHeight: Dp): ToolbarState {
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    var toolbarOffsetHeightPx by rememberSaveable { mutableStateOf(0f) }
    val nestedScrollConnection = retain {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx + delta
                val oldOffset = toolbarOffsetHeightPx
                toolbarOffsetHeightPx = newOffset.coerceIn(-toolbarHeightPx, 0f)
                val consumed = toolbarOffsetHeightPx - oldOffset
                return Offset(x = 0f, y = consumed)
            }
        }
    }
    return remember(toolbarOffsetHeightPx, nestedScrollConnection) {
        ToolbarState(
            height = toolbarHeight,
            offset = toolbarOffsetHeightPx,
            nestedScrollConnection = nestedScrollConnection,
            reset = { toolbarOffsetHeightPx = 0f },
        )
    }
}
