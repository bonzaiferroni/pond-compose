package pondui.ui.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import pondui.ui.behavior.ifNotNull
import pondui.ui.nav.LocalNav
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.NavRoute

@Composable
fun Modifier.actionable(route: NavRoute, isEnabled: Boolean = true): Modifier {
    val nav = LocalNav.current
    return this.actionable(route.title, isEnabled) { nav.go(route) }
}

@Composable
fun Modifier.actionable(
    hoverText: String? = null,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    return if (isEnabled) {
        this.ifNotNull(hoverText) {
                val source = remember { MutableInteractionSource() }
                val isHovered = source.collectIsHoveredAsState().value
                val portal = LocalPortal.current
                LaunchedEffect(isHovered) {
                    portal.setHoverText(if (isHovered) it else "")
                }
                this.hoverable(source)
            }
            .clickable(onClick = onClick)
    } else {
        this
    }
}
