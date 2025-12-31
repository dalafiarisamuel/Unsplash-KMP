package ui.component


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.arrow_back
import unsplashkmp.composeapp.generated.resources.photo_detail


@Composable
internal fun NavBar(
    modifier: Modifier = Modifier,
    showNavigationBackIcon: Boolean = true,
    navigationBarTitle: String = stringResource(Res.string.photo_detail),
    onBackPressed: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().then(modifier)
    ) {

        if (showNavigationBackIcon) {
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier.testTag("back_icon")
            ) {
                Icon(
                    painterResource(Res.drawable.arrow_back),
                    tint = appWhite,
                    contentDescription = null,
                )
            }
        }

        Text(
            text = navigationBarTitle,
            color = appWhite,
            fontSize = 22.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .testTag("toolbar_title")
                .align(Alignment.CenterVertically)
                .padding(start = if (showNavigationBackIcon) 24.dp else 0.dp)
        )
    }

}