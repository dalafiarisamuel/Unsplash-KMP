package ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PhotoLargeDisplay(
    modifier: Modifier,
    imageUrl: String,
    imageColor: String? = null,
    imageSize: Size,
) {
    var isScaled: Boolean by remember { mutableStateOf(false) }
    var showShimmer: Boolean by remember { mutableStateOf(true) }
    val scale: Float by animateFloatAsState(
        targetValue = if (isScaled) 1.45f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
            visibilityThreshold = 0.001f
        )
    )
    val aspectRatio: Float by remember {
        derivedStateOf { (imageSize.width / imageSize.height) }
    }
    val painter: AsyncImagePainter = rememberAsyncImagePainter(imageUrl)
    val painterState by painter.state.collectAsState()
    if (painterState is AsyncImagePainter.State.Success) {
        showShimmer = false
    }

    Box(modifier = Modifier.fillMaxWidth().then(modifier)) {
        Card(
            elevation = 1.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = modifier,
            onClick = { isScaled = !isScaled }) {
            Image(
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .background(
                        shimmerBrush(
                            targetValue = 1300f,
                            showShimmer = showShimmer,
                            shimmerColorFrame = imageColor
                        )
                    )
                    .aspectRatio(aspectRatio)
                    .scale(scale)
                    .width(350.dp)
                    .height(500.dp)
            )
        }
    }
}