package ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun CollapsingToolbarBase(
    modifier: Modifier = Modifier,
    toolbarHeight: Dp,
    minShrinkHeight: Dp = 0.dp,
    toolbarOffset: Float,
    content: @Composable BoxScope.() -> Unit,
) {
    val visibility by remember { mutableStateOf(MutableTransitionState(true)) }

    val scrollDp by derivedStateOf { toolbarHeight + toolbarOffset.dp }

    val animatedCardSize by
        animateDpAsState(
            targetValue = if (scrollDp <= minShrinkHeight) minShrinkHeight else scrollDp,
            animationSpec = tween(400, easing = LinearOutSlowInEasing),
        )
    visibility.targetState = animatedCardSize > 70.0.dp

    AnimatedVisibility(
        visibleState = visibility,
        enter = fadeIn(initialAlpha = 0.4f),
        exit = fadeOut(tween(durationMillis = 150)),
    ) {
        Box(modifier = modifier.height(animatedCardSize).fillMaxWidth(), content = content)
    }
}
