package pondui.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.User
import kotlinx.collections.immutable.ImmutableList
import pondui.io.LocalUserContext
import pondui.ui.controls.Icon
import pondui.ui.controls.Label
import pondui.ui.controls.actionable
import pondui.ui.theme.Pond
import pondui.utils.lighten
import pondui.utils.modifyIfTrue


@Composable
fun RowScope.PortalBarControls(
    portalItems: ImmutableList<PortalItem>
) {
    val nav = LocalNav.current
    val navState by nav.state.collectAsState()
    val currentRoute = navState.route
    val userContext = LocalUserContext.current
    val userContextState = userContext?.state?.collectAsState()?.value

    Row(
        modifier = Modifier.weight(1f)
    ) {
        for (item in portalItems) {
            val portalDoor = item as? PortalDoor
            if (portalDoor?.requireLogin == true && userContextState?.isLoggedIn != true) continue
            val route = portalDoor?.route
            PortalItemControl(
                icon = item.icon,
                label = item.label,
                isCurrentRoute = route == currentRoute,
                hoverText = route?.title
            ) {
                when (item) {
                    is PortalAction -> { item.action(nav) }
                    is PortalDoor -> { nav.go(item.route) }
                }
            }
        }
    }

    if (userContextState != null) {
        val label = userContextState.user?.username ?: "Log in"
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
            .clip(Pond.ruler.shroomed)
            .modifyIfTrue(!isCurrentRoute) { this.actionable(hoverText, onClick = onClick) }
            .modifyIfTrue(isCurrentRoute) { this.background(Pond.colors.secondary.lighten(.4f).copy(.2f))}
            .padding(Pond.ruler.innerPadding)
    ) {
        val color = when (isCurrentRoute) {
            true -> Pond.localColors.content
            false -> Pond.localColors.contentDim
        }
        Icon(
            imageVector = icon,
            tint = color,
            modifier = Modifier.weight(1f).aspectRatio(1f)
                .padding(Pond.ruler.innerPadding)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Label(label, color)
    }
}