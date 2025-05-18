package pondui.ui.controls

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import pondui.ui.nav.LocalNav
import pondui.ui.nav.NavRoute
import pondui.utils.modifyIfTrue
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors

@Composable
fun Button(
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    shape: Shape = Pond.ruler.round,
    modifier: Modifier = Modifier.background(background),
    content: @Composable BoxScope.() -> Unit
) {
    ProvideSkyColors {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.clip(shape)
                .modifyIfTrue(isEnabled) { clickable(onClick = onClick) }
                .graphicsLayer( alpha = if (isEnabled) 1f else .5f )
                .background(background)
                .padding(Pond.ruler.halfPadding)
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
fun FlowRowScope.ControlRowButton(
    text: String,
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) = Button(
    text = text,
    isEnabled = isEnabled,
    background = background,
    shape = RectangleShape,
    modifier = modifier.fillMaxRowHeight(),
    onClick = onClick,
)