package ui.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import ui.theme.color

@Composable
internal fun shimmerBrush(
    shimmerColorFrame: String?,
    showShimmer: Boolean = true,
    targetValue: Float = 1000f,
): Brush {

    return if (showShimmer) {
        val shimmerColors = remember { returnShimmerColors(shimmerColorFrame) }
        val transition = rememberInfiniteTransition()
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            )
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = 0f)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero,
        )
    }
}

private fun returnShimmerColors(color: String?): List<Color> {
    val imageColorParsed = (color?.color ?: Color.LightGray)
    return listOf(
        imageColorParsed.copy(alpha = 0.6f),
        imageColorParsed.copy(alpha = 0.2f),
        imageColorParsed.copy(alpha = 0.6f),
    )
}