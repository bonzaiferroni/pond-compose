package pondui.ui.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import pondui.ui.modifiers.ifTrue
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors

@Composable
fun ContentButton(
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    shape: Shape = Pond.ruler.pill,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val hoverFactor by animateFloatAsState(if (isHovered) 1f else 0f, animationSpec = tween(300))
    val clickFactor by animateFloatAsState(if (isPressed) 1f else 0f)

    ProvideSkyColors {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max)
                .ifTrue(isEnabled) {
                    clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                }
                .graphicsLayer {
                    val scale = 1f - clickFactor * .1f
                    alpha = if (isEnabled) 1f else .5f
                    this.shape = shape
                    clip = true
                    scaleY = scale
                    scaleX = scale
                }
        ) {
            content()
            Box(
                modifier = Modifier.fillMaxSize()
                    .drawBehind {
                    drawRect(Color.White.copy(hoverFactor * .2f))
                }
            )
        }
    }
}