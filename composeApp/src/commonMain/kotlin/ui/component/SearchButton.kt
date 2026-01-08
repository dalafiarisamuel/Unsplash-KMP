package ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ui.theme.appWhite
import ui.theme.colorDisabledGray
import ui.theme.colorGrayDivider
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.ic_search

@ExperimentalComposeUiApi
@Composable
internal fun SearchButton(
    modifier: Modifier = Modifier,
    onSearchButtonClicked: (() -> Unit)? = null,
) {
    IconButton(
        onClick = { onSearchButtonClicked?.invoke() },
        modifier =
            modifier
                .background(shape = RoundedCornerShape(27.5.dp), color = colorDisabledGray)
                .border(width = 1.dp, color = colorGrayDivider, shape = RoundedCornerShape(27.5.dp)),
    ) {
        Image(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(Res.drawable.ic_search),
            colorFilter = ColorFilter.tint(appWhite),
            contentScale = ContentScale.Inside,
            contentDescription = null,
        )
    }
}
