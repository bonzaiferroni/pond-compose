package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import pondui.ui.nav.LocalNav
import pondui.ui.nav.NavRoute
import pondui.ui.theme.Pond

@Composable
fun RouteButton(
    text: String,
    isEnabled: Boolean = true,
    background: Color = Pond.colors.accent,
    modifier: Modifier = Modifier,
    getRoute: () -> NavRoute
) {
    val nav = LocalNav.current
    Button(onClick = {
        nav.go(getRoute())
    }, isEnabled = isEnabled, color = background, modifier = modifier) {
        Text(
            text = text.uppercase(),
            style = TextStyle(fontSize = Pond.typo.label.fontSize),
        )
    }
}