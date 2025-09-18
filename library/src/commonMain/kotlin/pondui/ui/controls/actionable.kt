package pondui.ui.controls

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import pondui.ui.behavior.ifNotNull
import pondui.ui.nav.LocalNav
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.NavRoute

@Composable
fun Modifier.actionable(
    route: NavRoute,
    isEnabled: Boolean = true,
    onHover: ((Boolean) -> Unit)? = null,
    isIndicated: Boolean = true,
    icon: PointerIcon? = null,
): Modifier {
    val nav = LocalNav.current
    return this.actionable(
        hoverText = route.title,
        isEnabled = isEnabled,
        icon = icon,
        isIndicated = isIndicated,
        onHover = onHover,
    ) { nav.go(route) }
}

@Composable
fun Modifier.actionable(
    hoverText: String? = null,
    isEnabled: Boolean = true,
    onHover: ((Boolean) -> Unit)? = null,
    isIndicated: Boolean = true,
    icon: PointerIcon? = null,
    onClick: () -> Unit,
) = if (isEnabled) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered = interactionSource.collectIsHoveredAsState().value
    val portal = LocalPortal.current
    LaunchedEffect(isHovered) {
        hoverText?.let {
            portal.setHoverText(if (isHovered) it else "")
        }
        onHover?.invoke(isHovered)
    }
    val indication = if (isIndicated) LocalIndication.current else null
    this.clickable(onClick = onClick, interactionSource = interactionSource, indication = indication)
        .ifNotNull(icon) { pointerHoverIcon(it) }
} else {
    this
}
