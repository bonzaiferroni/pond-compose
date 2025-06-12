@file:Suppress("DuplicatedCode")

package pondui.ui.behavior

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Magic(
    isVisible: Boolean = true,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    rotationZ: Int = 0,
    rotationY: Int = 0,
    rotationX: Int = 0,
    durationMillis: Int = 500,
    scale: Boolean = false,
    fade: Boolean = true,
    exitOpposite: Boolean = false,
    isVisibleInit: Boolean = false,
    easing: Easing = FastOutSlowInEasing,
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit
) {
    var currentVisibility by remember { mutableStateOf(isVisibleInit) }
    LaunchedEffect(isVisible) {
        currentVisibility = isVisible
    }

    val animatedVisibility by animateFloatAsState(
        targetValue =  if (currentVisibility) 1f else 0f,
        animationSpec =  tween(durationMillis, easing = easing)
    )
    if (animatedVisibility == 0f) return

    val density = LocalDensity.current
    val offsetPxX = with(density) { offsetX.toPx() }
    val offsetPxY = with(density) { offsetY.toPx() }

    Box(
        modifier = modifier
            .graphicsLayer {
                val opposite = !isVisible && exitOpposite
                translationY = (if (opposite) -offsetPxY else offsetPxY) * (1 - animatedVisibility)
                translationX = (if (opposite) -offsetPxX else offsetPxX) * (1 - animatedVisibility)
                this.rotationX = (if (opposite) -rotationX else rotationX) * (1 - animatedVisibility)
                this.rotationY = (if (opposite) -rotationY else rotationY) * (1 - animatedVisibility)
                this.rotationZ = (if (opposite) -rotationZ else rotationZ) * (1 - animatedVisibility)
                if (fade) {
                    alpha = animatedVisibility
                }
                if (scale) {
                    scaleX = animatedVisibility
                    scaleY = animatedVisibility
                }
            }
    ) {
        content()
    }
}

@Composable
fun Modifier.magic(
    isVisible: Boolean = true,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    rotationZ: Int = 0,
    rotationY: Int = 0,
    rotationX: Int = 0,
    durationMillis: Int = 500,
    scale: Boolean = false,
    fade: Boolean = true,
    exitOpposite: Boolean = false,
    isVisibleInit: Boolean = false,
    easing: Easing = FastOutSlowInEasing,
): Modifier {
    var currentVisibility by remember { mutableStateOf(isVisibleInit) }
    LaunchedEffect(isVisible) {
        currentVisibility = isVisible
    }

    val animatedVisibility by animateFloatAsState(
        targetValue =  if (currentVisibility) 1f else 0f,
        animationSpec =  tween(durationMillis, easing = easing)
    )

    val density = LocalDensity.current
    val offsetPxX = with(density) { offsetX.toPx() }
    val offsetPxY = with(density) { offsetY.toPx() }

    return this.graphicsLayer {
        val opposite = !isVisible && exitOpposite
        translationY = (if (opposite) -offsetPxY else offsetPxY) * (1 - animatedVisibility)
        translationX = (if (opposite) -offsetPxX else offsetPxX) * (1 - animatedVisibility)
        this.rotationX = (if (opposite) -rotationX else rotationX) * (1 - animatedVisibility)
        this.rotationY = (if (opposite) -rotationY else rotationY) * (1 - animatedVisibility)
        this.rotationZ = (if (opposite) -rotationZ else rotationZ) * (1 - animatedVisibility)
        if (fade) {
            alpha = animatedVisibility
        }
        if (scale) {
            scaleX = animatedVisibility
            scaleY = animatedVisibility
        }
    }
}

//@Composable
//fun <T> FlipMagic(
//    item: T?,
//    modifier: Modifier = Modifier,
//    defaultContent: @Composable () -> Unit,
//    itemContent: @Composable (T) -> Unit
//) {
//    // val animatedRotation by animateFloatAsState(if)
//}