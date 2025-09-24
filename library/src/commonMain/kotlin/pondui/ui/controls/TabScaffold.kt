package pondui.ui.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import pondui.ui.modifiers.artBackground
import pondui.ui.nav.portalTopBarHeight
import pondui.ui.theme.Pond
import pondui.utils.darken

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun TabScaffold(
    drawerContent: (@Composable () -> Unit)? = null,
    content: @Composable TabScaffoldScope.() -> Unit
) {
    // val state by LocalPortal.current.stateFlow.collectAsState()
    val unitSpacing = Pond.ruler.unitSpacing
    // val bottomPadding = if (state.bottomBarIsVisible) portalBottomBarHeight + unitSpacing else 0.dp
    var topPadding by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val hazeState = remember { HazeState() }
    val tabScope = remember { TabScope() }
    val ruler = Pond.ruler
    val tabScaffoldScope = remember(topPadding) { TabScaffoldScope(topPadding + ruler.unitSpacing, tabScope) }

    Box {
        Box(
            modifier = Modifier.hazeSource(state = hazeState)
                .fillMaxSize()
        ) {
            TabContent(tabScaffoldScope.tabScope) {
                tabScaffoldScope.content()
            }
        }

        Column(
            modifier = Modifier.onGloballyPositioned { topPadding = with(density) { it.size.height.toDp() } }
        ) {
            Column(
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.thin(Pond.colors.void.darken(.1f))
                )
                    .fillMaxWidth()
                    .padding(
                        top = portalTopBarHeight + unitSpacing,
                        start = unitSpacing,
                        end = unitSpacing,
                        bottom = 0.dp,
                    )
            ) {
                drawerContent?.invoke()
            }
            Box(
                modifier = Modifier.clip(Pond.ruler.pillBottom)
                    .hazeEffect(state = hazeState, style = HazeMaterials.thin(Pond.colors.void.darken(.1f)))
                    .padding(Pond.ruler.unitSpacing)
            ) {
                TabHeader(tabScaffoldScope.tabScope, headerShape = Pond.ruler.pillBottomRoundedTop)
            }
        }
    }
}

class TabScaffoldScope(
    val topPadding: Dp,
    val tabScope: TabScope,
)

@Composable
fun TabScaffoldScope.Tab(
    label: String,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    content: @Composable () -> Unit,
) {
    val ruler = Pond.ruler
    tabScope.Tab(
        label = label,
        isVisible = isVisible,
        modifier = Modifier.fillMaxSize()
            .artBackground()
            .padding(
                top = topPadding,
                start = ruler.unitSpacing,
                end = ruler.unitSpacing,
            ).then(modifier),
        content = content
    )
}