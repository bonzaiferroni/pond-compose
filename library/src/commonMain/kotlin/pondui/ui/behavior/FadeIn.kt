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

@Composable
fun Magic(
    isVisible: Boolean = true,
    offsetX: Int = 0,
    offsetY: Int = 0,
    rotationZ: Int = 0,
    rotationY: Int = 0,
    rotationX: Int = 0,
    durationMillis: Int = 300,
    scale: Boolean = false,
    easing: Easing = FastOutSlowInEasing,
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit
) {
    var currentVisibility by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        currentVisibility = isVisible
    }

    val animatedVisibility by animateFloatAsState(
        targetValue =  if (currentVisibility) 1f else 0f,
        animationSpec =  tween(durationMillis, easing = easing)
    )
    if (animatedVisibility == 0f) return

    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = offsetY * (1 - animatedVisibility)
                translationX = offsetX * (1 - animatedVisibility)
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

@Composable
fun Modifier.magic(
    isVisible: Boolean = true,
    offsetX: Int = 0,
    offsetY: Int = 0,
    rotationZ: Int = 0,
    rotationY: Int = 0,
    rotationX: Int = 0,
    durationMillis: Int = 300,
    scale: Boolean = false,
    easing: Easing = FastOutSlowInEasing,
): Modifier {
    var currentVisibility by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        currentVisibility = isVisible
    }

    val animatedVisibility by animateFloatAsState(
        targetValue =  if (currentVisibility) 1f else 0f,
        animationSpec =  tween(durationMillis, easing = easing)
    )

    return this.graphicsLayer {
        translationY = offsetY * (1 - animatedVisibility)
        translationX = offsetX * (1 - animatedVisibility)
        this.rotationX = rotationX * (1 - animatedVisibility)
        this.rotationY = rotationY * (1 - animatedVisibility)
        this.rotationZ = rotationZ * (1 - animatedVisibility)
        alpha = animatedVisibility
        if (scale) {
            scaleX = animatedVisibility
            scaleY = animatedVisibility
        }
    }
}