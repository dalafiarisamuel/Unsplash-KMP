package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkRemove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.remote.model.UnsplashPhotoRemote
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.component.ArtistCard
import ui.component.ErrorComponent
import ui.component.NavBar
import ui.component.PhotoLargeDisplay
import ui.component.animateScrollIconAsState
import ui.component.getEdgeToEdgeTopPadding
import ui.state.PhotoDetailState
import ui.theme.ColorAquamarine
import ui.theme.ColorPalatinateBlue
import ui.theme.UnsplashKMPTheme
import ui.theme.appWhite
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.add_favourite
import unsplashkmp.composeapp.generated.resources.an_unknown_error_occurred
import unsplashkmp.composeapp.generated.resources.download_image
import unsplashkmp.composeapp.generated.resources.remove_favourite
import unsplashkmp.composeapp.generated.resources.round_downloading

@Composable
internal fun PhotoDetail(
    modifier: Modifier = Modifier,
    state: PhotoDetailState = PhotoDetailState(),
    showNavigationBackIcon: Boolean = true,
    onBookmarkClicked: (UnsplashPhotoRemote) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onDownloadImageClicked: (String?) -> Unit = {},
) {

    Column(
        modifier =
            Modifier.background(MaterialTheme.colors.background)
                .fillMaxSize()
                .padding(top = 10.dp + getEdgeToEdgeTopPadding(), start = 12.dp, end = 12.dp)
                .then(modifier)
    ) {
        NavBar(
            modifier = Modifier.defaultMinSize(minHeight = 30.dp).fillMaxWidth(),
            showNavigationBackIcon = showNavigationBackIcon,
            onBackPressed = onBackPressed,
        )

        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            when {
                state.isLoading -> {
                    LinearProgressIndicator(
                        color = appWhite,
                        modifier = Modifier.fillMaxWidth().height(2.dp),
                    )
                }

                state.error != null -> {
                    ErrorComponent(
                        modifier = Modifier.fillMaxSize().testTag("error_view"),
                        message =
                            state.error.message
                                ?: stringResource(Res.string.an_unknown_error_occurred),
                    )
                }

                else -> {

                    if (state.photo != null) {
                        Spacer(modifier = Modifier.padding(top = 10.dp))
                        ArtistCard(unsplashUser = state.photo.user)
                    }

                    Spacer(modifier = Modifier.padding(top = 10.dp))

                    PhotoLargeDisplay(
                        modifier = Modifier.fillMaxWidth(),
                        imageUrl = state.photo?.urls?.full.orEmpty(),
                        imageColor = state.photo?.color,
                        imageSize =
                            Size(
                                (state.photo?.width ?: 1).toFloat(),
                                (state.photo?.height ?: 1).toFloat(),
                            ),
                    )

                    if (
                        state.photo?.description != null ||
                            state.photo?.alternateDescription != null
                    ) {
                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        val content = state.photo.description ?: state.photo.alternateDescription

                        Text(
                            text = content.orEmpty().capitalize(Locale.current),
                            color = appWhite,
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                    }

                    Spacer(modifier = Modifier.padding(top = 20.dp))

                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        val iconPainter =
                            if (state.isImageFavourite) Icons.Rounded.BookmarkRemove
                            else Icons.Rounded.BookmarkAdd

                        val iconText =
                            stringResource(
                                if (state.isImageFavourite) Res.string.remove_favourite
                                else Res.string.add_favourite
                            )

                        val animatedIcon = animateScrollIconAsState(iconPainter)

                        OutlinedButton(
                            modifier = Modifier.height(height = 45.dp).width(width = 160.dp),
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
                            onClick = { onBookmarkClicked(state.photo!!) },
                        ) {
                            Icon(
                                painter = animatedIcon,
                                tint = ColorPalatinateBlue,
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = iconText, fontSize = 11.sp)
                        }
                        OutlinedButton(
                            modifier = Modifier.height(height = 45.dp).width(width = 190.dp),
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
                            onClick = { onDownloadImageClicked(state.photo?.urls?.raw) },
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.round_downloading),
                                tint = ColorAquamarine,
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = stringResource(Res.string.download_image), fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.padding(top = 30.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun PreviewPhotoDetail() {
    UnsplashKMPTheme { PhotoDetail() }
}
