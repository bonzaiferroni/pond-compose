package pondui.ui.modifiers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun Modifier.magicDim(isDim: Boolean, factor: Float = .2f): Modifier {
    val animation by animateFloatAsState(if (isDim) 1f else 0f)
    return this.graphicsLayer {
        alpha = 1f - factor * animation
    }
}