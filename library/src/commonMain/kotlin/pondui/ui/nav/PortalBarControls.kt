package pondui.ui.nav

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.TablerIcons
import compose.icons.tablericons.User
import kotlinx.collections.immutable.ImmutableList
import pondui.io.LocalUserContext
import pondui.ui.controls.Icon
import pondui.ui.controls.Label
import pondui.ui.controls.actionable
import pondui.ui.theme.Pond
import pondui.utils.modifyIfTrue


@Composable
fun RowScope.PortalBarControls(
    portalItems: ImmutableList<PortalItem>
) {
    val nav = LocalNav.current
    val navState by nav.state.collectAsState()
    val currentRoute = navState.route
    val userContext = LocalUserContext.current
    val userContextState = userContext?.state?.collectAsState()

    Row(
        horizontalArrangement = Pond.ruler.rowTight,
        modifier = Modifier.weight(1f)
    ) {
        for (item in portalItems) {
            val portalRoute = item as? PortalRoute
            if (portalRoute?.requireLogin == true && userContextState?.value?.isLoggedIn != true) continue
            val route = portalRoute?.route
            PortalItemControl(
                icon = item.icon,
                label = item.label,
                isCurrentRoute = route == currentRoute,
                hoverText = route?.title
            ) {
                when (item) {
                    is PortalAction -> { item.action(nav) }
                    is PortalRoute -> { nav.go(item.route) }
                }
            }
        }
    }

    if (userContextState != null) {
        val label = userContextState.value.user?.username ?: "Log in"
        PortalItemControl(
            icon = TablerIcons.User,
            label = label,
            isCurrentRoute = false,
            hoverText = null,
        ) {
            userContext.toggle()
        }
    }
}

@Composable
fun PortalItemControl(
    icon: ImageVector,
    label: String,
    isCurrentRoute: Boolean,
    hoverText: String?,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
            .aspectRatio(1f)
            .modifyIfTrue(!isCurrentRoute) { this.actionable(hoverText, onClick = onClick) }
    ) {
        val color = when (isCurrentRoute) {
            true -> Pond.colors.shine
            false -> Pond.localColors.contentDim
        }
        Icon(
            imageVector = icon,
            tint = color,
            modifier = Modifier.weight(1f).aspectRatio(1f)
        )
        Label(label, color)
    }
}