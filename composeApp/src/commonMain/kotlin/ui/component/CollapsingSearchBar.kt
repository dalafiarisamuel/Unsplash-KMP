package ui.component


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.UnsplashKMPTheme
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.search_images


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
internal fun CollapsibleSearchBar(
    modifier: Modifier = Modifier,
    toolbarOffset: Float = 0f,
    toolbarHeight: Dp = 100.dp,
    minShrinkHeight: Dp = 0.dp,
    textValue: String? = null,
    keyboardAction: (() -> Unit)? = null,
    textValueChange: ((String) -> Unit)? = null,
) {

    val keyboard = LocalSoftwareKeyboardController.current

    CollapsingToolbarBase(
        modifier = modifier.wrapContentSize(),
        toolbarHeight = toolbarHeight,
        toolbarOffset = toolbarOffset,
        minShrinkHeight = minShrinkHeight,
    ) {

        Column(
            modifier = Modifier
                .wrapContentHeight()
        ) {

            Text(
                text = stringResource(Res.string.search_images),
                color = appWhite,
                fontSize = 13.sp,
                fontStyle = FontStyle.Normal,
                modifier = Modifier.padding(top = 25.dp)
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
            ) {

                UnsplashSearchBox(
                    modifier = Modifier
                        .wrapContentHeight(),
                    textValue = textValue,
                    keyboard = keyboard,
                    keyboardAction = keyboardAction
                ) {
                    textValueChange?.invoke(it)
                }
            }
        }

    }
}

@Preview
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
private fun PreviewCollapsibleSearchBar() {
    UnsplashKMPTheme {
        CollapsibleSearchBar()
    }
}