package ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter

@Composable
fun animateScrollPainterAsState(targetPainter: Painter): Painter {
    // Keep track of the previous painter so we can draw the exit animation
    var previousPainter by remember { mutableStateOf<Painter?>(null) }

    // Animate from 0.0 to 1.0.
    // We will use this progress to calculate both Alpha and Vertical Offset
    val progress = remember { Animatable(0f) }

    LaunchedEffect(targetPainter) {
        // Before switching, capture the current target as the "previous" one
        // but only if it's actually different to avoid looping
        if (previousPainter != targetPainter) {
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = tween(400))
            previousPainter = targetPainter
        }
    }

    return remember(targetPainter, progress.value) {
        object : Painter() {
            override val intrinsicSize: Size = targetPainter.intrinsicSize

            override fun DrawScope.onDraw() {
                val currentProgress = progress.value
                val height = size.height

                // 1. Draw Previous Painter (Scrolling OUT to the bottom)
                previousPainter?.let { oldPainter ->
                    if (currentProgress < 1f) {
                        val exitOffset = height * currentProgress // Moves from 0 down to Height
                        translate(top = exitOffset) {
                            with(oldPainter) { draw(size, alpha = 1f - currentProgress) }
                        }
                    }
                }

                // 2. Draw Target Painter (Scrolling IN from the top)
                val entryOffset = -height * (1f - currentProgress) // Moves from -Height to 0
                translate(top = entryOffset) {
                    with(targetPainter) { draw(size, alpha = currentProgress) }
                }
            }
        }
    }
}

@Composable
fun animateScrollIconAsState(targetIcon: ImageVector): Painter {
    // Convert the target vector into a painter
    val targetPainter = rememberVectorPainter(targetIcon)

    // Keep track of the previous painter for the exit animation
    var previousPainter by remember { mutableStateOf<Painter?>(null) }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(targetIcon) {
        // We check targetIcon here because Painters can be recreated on recomposition
        if (previousPainter != null) {
            progress.snapTo(0f)
            progress.animateTo(1f, animationSpec = tween(400))
        }
        previousPainter = targetPainter
    }

    return remember(targetPainter, progress.value) {
        object : Painter() {
            override val intrinsicSize: Size = targetPainter.intrinsicSize

            override fun DrawScope.onDraw() {
                val currentProgress = progress.value
                val height = size.height

                // 1. Draw Previous Icon (Scrolling OUT to the bottom)
                previousPainter?.let { oldPainter ->
                    if (currentProgress < 1f) {
                        val exitOffset = height * currentProgress
                        translate(top = exitOffset) {
                            with(oldPainter) { draw(size, alpha = 1f - currentProgress) }
                        }
                    }
                }

                // 2. Draw Target Icon (Scrolling IN from the top)
                val entryOffset = -height * (1f - currentProgress)
                translate(top = entryOffset) {
                    with(targetPainter) {
                        draw(
                            size,
                            alpha = if (previousPainter == targetPainter) 1f else currentProgress,
                        )
                    }
                }
            }
        }
    }
}
