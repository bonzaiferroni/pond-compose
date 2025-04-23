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
    // val navGraph: NavGraphBuilder.() -> Unit,
    val doors: ImmutableList<PortalItem>,
    val routes: ImmutableList<RouteConfig>
) {
    fun toRoute(path: String) = routes.firstNotNullOfOrNull { it.toRoute?.invoke(path) }
}

data class RouteConfig(
    val content: NavGraphBuilder.() -> Unit,
    val toRoute: ((String) -> NavRoute?)? = null,
)