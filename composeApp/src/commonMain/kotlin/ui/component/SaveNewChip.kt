package ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.usecase.preference.SaveSearchQueryUseCase
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.theme.ColorCrimsonRed
import ui.theme.ColorPalatinateBlue
import ui.theme.appWhite
import ui.theme.colorDisabledGray
import ui.theme.colorGrayDivider
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.add_search_chip
import unsplashkmp.composeapp.generated.resources.create_search_chip
import unsplashkmp.composeapp.generated.resources.ic_image
import unsplashkmp.composeapp.generated.resources.search_image_hint

@Composable
fun SaveNewChip(isDialogVisible: Boolean, onSave: (String) -> Unit) {

    var textValue by remember { mutableStateOf("") }

    if (isDialogVisible.not()) {
        textValue = ""
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 20.dp).imePadding(),
    ) {
        Box(
            modifier =
                Modifier.align(Alignment.CenterHorizontally)
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = Color.Gray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp),
                    )
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(Res.string.create_search_chip),
            color = appWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier =
                Modifier.background(shape = RoundedCornerShape(27.5.dp), color = colorDisabledGray)
                    .border(
                        width = 1.dp,
                        color = colorGrayDivider,
                        shape = RoundedCornerShape(27.5.dp),
                    )
                    .wrapContentHeight()
        ) {
            TextField(
                modifier =
                    Modifier.testTag("search_text_field")
                        .height(55.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                placeholder = {
                    Text(
                        text = stringResource(Res.string.search_image_hint),
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Normal,
                    )
                },
                colors =
                    TextFieldDefaults.textFieldColors(
                        textColor = appWhite,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                value = textValue,
                keyboardOptions =
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                singleLine = true,
                onValueChange = {
                    if (it.length <= SaveSearchQueryUseCase.MAX_QUERY_LENGTH) textValue = it
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(Res.drawable.ic_image),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(appWhite),
                        modifier = Modifier.padding(start = 21.dp).align(Alignment.CenterVertically),
                    )
                },
                trailingIcon = {
                    Text(
                        text = "${textValue.length}/${SaveSearchQueryUseCase.MAX_QUERY_LENGTH}",
                        color =
                            if (textValue.length >= SaveSearchQueryUseCase.MAX_QUERY_LENGTH)
                                ColorCrimsonRed
                            else Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                },
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedButton(
            enabled = textValue.isNotBlank(),
            modifier =
                Modifier.height(height = 45.dp)
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
            elevation =
                ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp,
                ),
            interactionSource = remember { MutableInteractionSource() },
            shape = RoundedCornerShape(10.dp),
            onClick = { onSave(textValue) },
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                tint = if (textValue.isNotBlank()) ColorPalatinateBlue else colorGrayDivider,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(Res.string.add_search_chip), fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}
