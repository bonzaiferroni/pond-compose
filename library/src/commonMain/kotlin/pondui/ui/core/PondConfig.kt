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
    val navGraph: NavGraphBuilder.() -> Unit,
    val portalItems: ImmutableList<PortalItem>,
)
