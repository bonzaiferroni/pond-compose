package newsref.app.pond.core

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import kotlinx.collections.immutable.ImmutableList
import newsref.app.pond.nav.*

data class PondConfig(
    val name: String,
    val logo: ImageVector,
    val home: NavRoute,
    val navGraph: NavGraphBuilder.() -> Unit,
    val portalItems: ImmutableList<PortalItem>,
)
