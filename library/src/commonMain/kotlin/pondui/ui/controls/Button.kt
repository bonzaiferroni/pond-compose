package pondui.ui.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.nav.LocalNav
import pondui.ui.nav.NavRoute
import pondui.ui.modifiers.ifTrue
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors
import pondui.utils.darken
import pondui.utils.glowWith
import pondui.utils.lighten

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.accent,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.buttonPadding,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val bg = if (isPressed) color.lighten(.3f) else if (isHovered) color.lighten(.1f) else color
    val animatedBackground by animateColorAsState(bg, animationSpec = tween(300))
    val animatedScale by animateFloatAsState(if (isPressed) .9f else 1f)
    val glow = Pond.colors.glow.darken(.1f)

    ProvideSkyColors {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .ifTrue(isEnabled) {
                    clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                }
                .graphicsLayer {
                    alpha = if (isEnabled) 1f else .5f
                    scaleY = animatedScale
                    scaleX = animatedScale
                }
                .drawBehind {
                    val gradient = animatedBackground.glowWith(glow, size)
                    drawRoundRect(gradient, alpha = .75f, cornerRadius = CornerRadius(size.height))
                    drawRoundRect(
                        Color.White.copy(.2f),
                        style = Stroke(3f.dp.toPx()),
                        cornerRadius = CornerRadius(size.height)
                    )
                }
                .padding(padding)
        ) {
            content()
        }
    }
}

@Composable
fun Button(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.accent,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.buttonPadding,
    minWidth: Dp? = 100.dp,
    onClick: () -> Unit,
) {
    val modifier = minWidth?.let { modifier.widthIn(min = it) } ?: modifier
    Button(
        onClick = onClick,
        isEnabled = isEnabled,
        color = color,
        padding = padding,
        modifier = modifier
    ) {
        Text(
            text = text.uppercase(),
            style = Pond.typo.small,
            maxLines = 1
        )
    }
}

@Composable
fun Button(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.accent,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.unitPadding,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        isEnabled = isEnabled,
        color = color,
        padding = padding,
        modifier = modifier,
    ) {
        Icon(
            imageVector = imageVector,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun NavButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.accent,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.doublePadding,
    onClick: () -> NavRoute
) {
    val nav = LocalNav.current
    Button(
        text = text,
        onClick = { nav.go(onClick()) },
        isEnabled = isEnabled,
        padding = padding,
        color = color,
        modifier = modifier
    )
}