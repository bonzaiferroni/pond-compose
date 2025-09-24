package pondui.ui.modifiers

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.magicBackground(color: Color): Modifier {
    val animatedColor by animateColorAsState(color)
    return this.drawBehind { drawRect(animatedColor) }
}