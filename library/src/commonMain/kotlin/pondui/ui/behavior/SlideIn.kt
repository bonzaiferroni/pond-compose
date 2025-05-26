package pondui.ui.behavior

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import coil3.size.Scale

@Composable
fun SlideIn(
    isVisible: Boolean = true,
    factor: Float = 1f,
    enter: EnterTransition = slideInVertically { (it * factor).toInt() },
    exit: ExitTransition = slideOutVertically { (it * factor).toInt() },
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit
) {
    var currentVisibility by remember { mutableStateOf(false)}
    LaunchedEffect(isVisible) {
        currentVisibility = isVisible
    }

    AnimatedVisibility(
        visible = currentVisibility,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun FadeIn(
    isVisible: Boolean = true,
    offsetX: Int = 0,
    offsetY: Int = 0,
    rotationZ: Int = 0,
    rotationY: Int = 0,
    rotationX: Int = 0,
    scale: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit
) {
    var currentVisibility by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        currentVisibility = isVisible
    }

    val animatedVisibility by animateFloatAsState(if (currentVisibility) 1f else 0f)
    if (animatedVisibility == 0f) return

    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = offsetX * (1 - animatedVisibility)
                translationX = offsetY * (1 - animatedVisibility)
                this.rotationX = rotationX * (1 - animatedVisibility)
                this.rotationY = rotationY * (1 - animatedVisibility)
                this.rotationZ = rotationZ * (1 - animatedVisibility)
                alpha = animatedVisibility
                if (scale) {
                    scaleX = animatedVisibility
                    scaleY = animatedVisibility
                }
            }
    ) {
        content()
    }
}