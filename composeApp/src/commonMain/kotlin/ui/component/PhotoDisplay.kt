package ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter

@Composable
internal fun PhotoLargeDisplay(
    modifier: Modifier,
    imageUrl: String,
    imageColor: String? = null,
    imageSize: Size,
) {
    var isScaled by remember { mutableStateOf(false) }
    var showShimmer by remember { mutableStateOf(true) }
    var tapOffset by remember { mutableStateOf(TransformOrigin.Center) }

    val scale by animateFloatAsState(
        targetValue = if (isScaled) 1.7f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        finishedListener = { if (it == 1f) tapOffset = TransformOrigin.Center }
    )

    val aspectRatio by remember { derivedStateOf { (imageSize.width / imageSize.height) } }
    val painter = rememberAsyncImagePainter(imageUrl)
    val painterState by painter.state.collectAsStateWithLifecycle()

    if (painterState is AsyncImagePainter.State.Success) {
        showShimmer = false
    }

    Box(modifier = Modifier.fillMaxWidth().then(modifier)) {
        Card(
            elevation = 1.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (!isScaled) {
                            tapOffset = TransformOrigin(
                                pivotFractionX = offset.x / size.width,
                                pivotFractionY = offset.y / size.height,
                            )
                            isScaled = true
                        } else {
                            isScaled = false
                        }
                    }
                },
        ) {
            Image(
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .background(
                        shimmerBrush(
                            targetValue = 1300f,
                            showShimmer = showShimmer,
                            shimmerColorFrame = imageColor,
                        )
                    )
                    .aspectRatio(aspectRatio)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        transformOrigin = tapOffset,
                        clip = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .width(350.dp)
                    .height(500.dp),
            )
        }
    }
}
