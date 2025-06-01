package pondui.ui.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import pondui.ui.nav.LocalNav
import pondui.ui.nav.NavRoute
import pondui.ui.behavior.modifyIfTrue
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors

@Composable
fun Button(
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    shape: Shape = Pond.ruler.round,
    padding: PaddingValues = Pond.ruler.doublePadding,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val animatedBackground by animateColorAsState(background, animationSpec = tween(500))

    ProvideSkyColors {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.clip(shape)
                .modifyIfTrue(isEnabled) { clickable(onClick = onClick) }
                .graphicsLayer { alpha = if (isEnabled) 1f else .5f }
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
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    shape: Shape = Pond.ruler.round,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        isEnabled = isEnabled,
        background = background,
        shape = shape,
        modifier = modifier
    ) {
        Text(
            text = text.uppercase(),
            style = TextStyle(fontSize = Pond.typo.label.fontSize),
        )
    }
}

@Composable
fun Button(
    imageVector: ImageVector,
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    shape: Shape = Pond.ruler.round,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        isEnabled = isEnabled,
        background = background,
        shape = shape,
        padding = Pond.ruler.unitPadding,
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
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    modifier: Modifier = Modifier,
    onClick: () -> NavRoute
) {
    val nav = LocalNav.current
    Button(onClick = { nav.go(onClick()) }, isEnabled = isEnabled, background = background, modifier = modifier) {
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
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) = Button(
    text = text,
    isEnabled = isEnabled,
    background = background,
    shape = Pond.ruler.unitCorners,
    modifier = modifier,
    onClick = onClick,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowScope.ControlSetButton(
    imageVector: ImageVector,
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) = Button(
    imageVector = imageVector,
    isEnabled = isEnabled,
    background = background,
    shape = Pond.ruler.unitCorners,
    modifier = modifier.fillMaxRowHeight(),
    onClick = onClick,
)