package io.pondlib.compose.theme.core

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import io.pondlib.compose.theme.nav.NavRoute
import io.pondlib.compose.theme.nav.PortalItem
import kotlinx.collections.immutable.ImmutableList

data class PondConfig(
    val name: String,
    val logo: ImageVector,
    val home: NavRoute,
    val navGraph: NavGraphBuilder.() -> Unit,
    val portalItems: ImmutableList<PortalItem>,
)
