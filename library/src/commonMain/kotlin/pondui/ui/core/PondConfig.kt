package pondui.ui.core

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import kotlinx.collections.immutable.ImmutableList
import pondui.ui.nav.NavRoute
import pondui.ui.nav.PortalItem

data class PondConfig(
    val name: String,
    val logo: ImageVector,
    val home: NavRoute,
    val routes: ImmutableList<RouteConfig>,
    val doors: ImmutableList<PortalItem>
) {
    fun toRoute(path: String) = routes.firstNotNullOfOrNull { it.toRoute?.invoke(path) }
}

data class RouteConfig(
    val toRoute: ((String) -> NavRoute?)? = null,
    val content: NavGraphBuilder.() -> Unit,
)