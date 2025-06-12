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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import pondui.ui.nav.LocalNav
import pondui.ui.nav.NavRoute
import pondui.ui.behavior.ifTrue
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors
import pondui.utils.lighten

@Composable
fun Button(
    onClick: () -> Unit,
    background: Color = Pond.colors.primary,
    isEnabled: Boolean = true,
    shape: Shape = Pond.ruler.pill,
    padding: PaddingValues = Pond.ruler.doublePadding,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val bg = if (isPressed) background.lighten(.3f) else if (isHovered) background.lighten(.1f) else background
    val animatedBackground by animateColorAsState(bg, animationSpec = tween(300))
    val animatedScale by animateFloatAsState(if (isPressed) .9f else 1f)

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
                    this.shape = shape
                    clip = true
                    scaleY = animatedScale
                    scaleX = animatedScale
                }
                .drawBehind {
                    drawRect(animatedBackground)
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
    background: Color = Pond.colors.primary,
    isEnabled: Boolean = true,
    shape: Shape = Pond.ruler.pill,
    padding: PaddingValues = Pond.ruler.doublePadding,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        isEnabled = isEnabled,
        background = background,
        shape = shape,
        padding = padding,
        modifier = modifier
    ) {
        Text(
            text = text.uppercase(),
            style = Pond.typo.small,
        )
    }
}

@Composable
fun Button(
    imageVector: ImageVector,
    background: Color = Pond.colors.primary,
    isEnabled: Boolean = true,
    shape: Shape = Pond.ruler.pill,
    padding: PaddingValues = Pond.ruler.doublePadding,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        isEnabled = isEnabled,
        background = background,
        shape = shape,
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
    background: Color = Pond.colors.primary,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.doublePadding,
    modifier: Modifier = Modifier,
    onClick: () -> NavRoute
) {
    val nav = LocalNav.current
    Button(
        onClick = { nav.go(onClick()) },
        isEnabled = isEnabled,
        padding = padding,
        background = background,
        modifier = modifier
    ) {
        Text(
            text = text.uppercase(),
            style = TextStyle(fontSize = Pond.typo.label.fontSize),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowScope.ControlSetButton(
    text: String,
    background: Color = Pond.colors.primary,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.doublePadding,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) = Button(
    text = text,
    isEnabled = isEnabled,
    background = background,
    shape = Pond.ruler.unitCorners,
    padding = padding,
    modifier = modifier,
    onClick = onClick,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowScope.ControlSetButton(
    imageVector: ImageVector,
    background: Color = Pond.colors.primary,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.doublePadding,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) = Button(
    imageVector = imageVector,
    isEnabled = isEnabled,
    background = background,
    shape = Pond.ruler.unitCorners,
    padding = padding,
    modifier = modifier.fillMaxRowHeight(),
    onClick = onClick,
)