package pondui.ui.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ThumbUp
import pondui.ui.theme.Pond
import pondui.utils.lighten

@Composable
fun Checkbox(
    value: Boolean,
    modifier: Modifier = Modifier,
    onChange: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val animate by animateFloatAsState(if (value) 1f else 0f)
    val background = Pond.colors.void
    val bg = if (isPressed) background.lighten(.3f) else if (isHovered) background.lighten(.1f) else background
    val animatedBackground by animateColorAsState(bg, animationSpec = tween(300))

    val animatedCorner by animateDpAsState(if (isHovered) Pond.ruler.unitCorner * 2 else Pond.ruler.unitCorner)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(CheckboxSize)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onChange(!value) }
            )
            .drawBehind {
                val corners = CornerRadius(x = animatedCorner.toPx(), y = animatedCorner.toPx())
                drawRoundRect(
                    color = animatedBackground,
                    cornerRadius = corners,
                )
                drawRoundRect(
                    color = animatedBackground.lighten(.3f),
                    cornerRadius = corners,
                    style = Stroke(2.5f.dp.toPx())
                )
            }
            .padding(Pond.ruler.halfPadding)
    ) {
        Icon(
            imageVector = TablerIcons.ThumbUp,
            color = Pond.colors.contentSky,
            modifier = Modifier.graphicsLayer {
                scaleX = animate
                scaleY = animate
                rotationZ = 360 * animate
                transformOrigin = TransformOrigin.Center
            }
        )
    }
}

@Composable
fun LabeledCheckbox(
    label: String,
    value: Boolean,
    onValueChanged: (Boolean) -> Unit,
) {
    Row(
        gap = 1,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(value, onChange = onValueChanged)
        Text(label)
    }
}

private val CheckboxSize = 26.dp
