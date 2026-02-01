package ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ui.theme.appDark
import ui.theme.appWhite

@Composable
internal fun ChipButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Rounded.Add,
    onClick: () -> Unit = {},
) {
    Surface(modifier = modifier, elevation = 2.dp, shape = CircleShape, color = appDark) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier =
                Modifier.size(35.dp)
                    .clickable(
                        onClick = onClick,
                        role = Role.Button,
                        interactionSource = remember { MutableInteractionSource() },
                    ),
        ) {
            Icon(
                imageVector = imageVector,
                tint = appWhite,
                contentDescription = null,
                modifier = Modifier,
            )
        }
    }
}
