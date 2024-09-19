package ui.component


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.appWhite
import ui.theme.colorDisabledGray
import ui.theme.colorGrayDivider
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.ic_image
import unsplashkmp.composeapp.generated.resources.search_image_hint

@ExperimentalComposeUiApi
@Preview
@Composable
fun UnsplashSearchBox(
    modifier: Modifier = Modifier,
    textValue: String? = null,
    keyboard: SoftwareKeyboardController? = null,
    keyboardAction: (() -> Unit)? = null,
    textValueChange: ((String) -> Unit)? = null,
) {

    Row(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(27.5.dp),
                color = colorDisabledGray
            )
            .border(
                width = 1.dp,
                color = colorGrayDivider,
                shape = RoundedCornerShape(27.5.dp)
            )
            .wrapContentHeight()
    ) {

        TextField(
            modifier = Modifier
                .testTag("search_text_field")
                .height(55.dp)
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            placeholder = {
                Text(
                    text = stringResource(Res.string.search_image_hint),
                    color = Color.DarkGray,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Normal
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = appWhite,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
            value = textValue ?: "",
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardAction?.invoke()
                    keyboard?.hide()
                }),
            singleLine = true,
            onValueChange = {
                textValueChange?.invoke(it)
            },
            leadingIcon = {
                Image(
                    painter = painterResource(Res.drawable.ic_image),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(appWhite),
                    modifier = Modifier
                        .padding(start = 21.dp)
                        .align(Alignment.CenterVertically)
                )
            },
            trailingIcon = {
                SearchButton(
                    modifier = Modifier
                        .testTag("search_button")
                        .size(55.dp)
                ) {
                    keyboard?.hide()
                    keyboardAction?.invoke()
                }
            }
        )
    }

}