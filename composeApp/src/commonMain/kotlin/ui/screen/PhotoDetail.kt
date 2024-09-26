package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.component.ArtistCard
import ui.component.ErrorComponent
import ui.component.NavBar
import ui.component.PhotoLargeDisplay
import ui.component.getEdgeToEdgeTopPadding
import ui.state.PhotoDetailState
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.an_unknown_error_occurred
import unsplashkmp.composeapp.generated.resources.download_image
import unsplashkmp.composeapp.generated.resources.round_downloading


@Preview
@Composable
internal fun PhotoDetail(
    modifier: Modifier = Modifier,
    state: PhotoDetailState = PhotoDetailState(),
    onBackPressed: () -> Unit = {},
    onDownloadImageClicked: (String?) -> Unit = {},
) {

    val scrollableState = rememberScrollState()
    val locale = Locale.current

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .padding(top = 10.dp + getEdgeToEdgeTopPadding(), start = 12.dp, end = 12.dp)
            .then(modifier)
    ) {

        NavBar(
            modifier = Modifier
                .defaultMinSize(minWidth = 25.dp)
                .fillMaxWidth(),
            onBackPressed = onBackPressed
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollableState)
        ) {
            when {
                state.isLoading -> {
                    LinearProgressIndicator(
                        color = appWhite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                    )
                }

                state.error != null -> {
                    ErrorComponent(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("error_view"),
                        message = state.error.message ?: stringResource(
                            Res.string.an_unknown_error_occurred
                        )
                    )
                }

                else -> {

                    if (state.photo != null) {
                        Spacer(modifier = Modifier.padding(top = 10.dp))
                        ArtistCard(unsplashUser = state.photo.user)
                    }

                    Spacer(modifier = Modifier.padding(top = 10.dp))

                    PhotoLargeDisplay(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(2f),
                        imageUrl = state.photo?.urls?.full.orEmpty(),
                        imageColor = state.photo?.color,
                        imageSize = Size(
                            (state.photo?.width ?: 1).toFloat(),
                            (state.photo?.height ?: 1).toFloat()
                        )
                    )

                    if (state.photo?.description != null || state.photo?.alternateDescription != null) {
                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        val content = state.photo.description ?: state.photo.alternateDescription

                        Text(
                            text = content.orEmpty().capitalize(locale),
                            color = appWhite,
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.padding(top = 20.dp))

                    OutlinedButton(
                        modifier = Modifier
                            .height(height = 45.dp)
                            .width(width = 200.dp)
                            .align(Alignment.CenterHorizontally),
                        elevation = ButtonDefaults.elevation(0.dp),
                        shape = RoundedCornerShape(10.dp),
                        onClick = { onDownloadImageClicked(state.photo?.urls?.raw) }
                    ) {
                        Icon(
                            painterResource(Res.drawable.round_downloading),
                            tint = appWhite,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = stringResource(Res.string.download_image), fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.padding(top = 30.dp))
                }
            }
        }
    }
}

