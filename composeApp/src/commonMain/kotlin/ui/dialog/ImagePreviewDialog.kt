package ui.dialog

import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import data.model.ui.Photo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.color
import ui.theme.complementary
import unsplashkmp.composeapp.generated.resources.Res
import unsplashkmp.composeapp.generated.resources.ic_image


@Composable
internal fun ImagePreviewDialog(photo: Photo?, onDismissCLicked: () -> Unit) {

    Dialog(
        onDismissRequest = onDismissCLicked,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        DialogContent(
            imageUrl = photo?.urls?.regular,
            imageColor = photo?.color,
            blurHash = photo?.blurHash,
            imageWidth = photo?.width,
            imageHeight = photo?.height,
            authorOrDescriptionText = photo?.description ?: photo?.alternateDescription
            ?: photo?.user?.name
        )

    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Preview
@Composable
private fun DialogContent(
    imageUrl: String? = null,
    imageColor: String? = null,
    blurHash: String? = null,
    imageWidth: Int? = null,
    imageHeight: Int? = null,
    authorOrDescriptionText: String? = null,
) {
    val imageColorParsed = (imageColor?.color ?: Color(0xFF212121))
    val imageColorParseComplementary = imageColorParsed.complementary()
    val aspectRatio: Float by remember {
        derivedStateOf { (imageWidth?.toFloat() ?: 1.0F) / (imageHeight?.toFloat() ?: 1.0F) }
    }

    val windowSizeClass = calculateWindowSizeClass()

    val frameSize: Pair<Float, Float> by remember {
        derivedStateOf {
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> 400F to 500F
                WindowWidthSizeClass.Medium -> 400F to 500F
                else -> 600F to 700F
            }
        }
    }

    val animatedSize by animateSizeAsState(
        targetValue = Size(frameSize.first, frameSize.second),
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        modifier = Modifier
            .size(width = animatedSize.width.dp, height = animatedSize.height.dp)
    ) {

        Card(
            backgroundColor = imageColorParsed,
            elevation = 0.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .aspectRatio(aspectRatio)
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    imageColorParsed,
                    shape = RoundedCornerShape(
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )
                )
                .align(Alignment.BottomCenter)
        ) {

            Image(
                painter = painterResource(Res.drawable.ic_image),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically),
                colorFilter = ColorFilter.tint(
                    color = imageColorParseComplementary
                )
            )

            Text(
                text = authorOrDescriptionText.orEmpty(),
                color = imageColorParseComplementary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .align(Alignment.CenterVertically)
                    .basicMarquee()
            )

        }
    }

}