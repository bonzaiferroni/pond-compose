package pondui.ui.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import pondui.ui.modifiers.artBackground
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.portalBottomBarHeight
import pondui.ui.nav.portalTopBarHeight
import pondui.ui.theme.Pond
import pondui.utils.darken

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun TabScaffold(
    drawerContent: (@Composable () -> Unit)? = null,
    content: @Composable TabContentScope.() -> Unit
) {
    val unitSpacing = Pond.ruler.unitSpacing
    // val bottomPadding = if (state.bottomBarIsVisible) portalBottomBarHeight + unitSpacing else 0.dp
    val density = LocalDensity.current
    val hazeState = remember { HazeState() }
    val scope = remember { TabScope() }
    val contentScope = remember { TabContentScope(scope) }
    contentScope.content()
    val tabs = contentScope.toTabs()

    Box {
        Box(
            modifier = Modifier.hazeSource(state = hazeState)
                .fillMaxSize()
        ) {
            TabContent(scope)
        }

        Column(
            modifier = Modifier.onGloballyPositioned {
                with(density) { scope.setTopPadding(it.size.height.toDp()) }
            }
        ) {
            Column(
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.thin(Pond.colors.void.darken(.1f))
                )
                    .fillMaxWidth()
                    .padding(
                        top = portalTopBarHeight,
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
                TabHeader(scope, tabs = tabs, headerShape = Pond.ruler.pillBottomRoundedTop)
            }
        }
    }
}

@Composable
fun TabContentScope.ScaffoldTab(
    label: String,
    isVisible: Boolean = true,
    content: @Composable () -> Unit,
) {
    Tab(
        label = label,
        isVisible = isVisible,
    ) {
        val state by tabScope.state.collectAsState()
        val ruler = Pond.ruler
        val portalState by LocalPortal.current.stateFlow.collectAsState()
        val bottomBarHeight = if (portalState.bottomBarIsVisible) portalBottomBarHeight else 0.dp

        Box(
            modifier = Modifier.fillMaxSize()
                .artBackground()
                .padding(
                    top = state.topPadding + ruler.unitSpacing,
                    start = ruler.unitSpacing,
                    end = ruler.unitSpacing,
                    bottom = bottomBarHeight
                )
        ) {
            content()
        }
    }
}

@Composable
fun TabContentScope.LazyColumnTab(
    label: String,
    gap: Int,
    isVisible: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    Tab(
        label = label,
        isVisible = isVisible,
    ) {
        val state by tabScope.state.collectAsState()
        val ruler = Pond.ruler
        LazyColumn(
            gap, modifier = Modifier.fillMaxSize()
                .artBackground()
                .padding(
                    start = ruler.unitSpacing,
                    end = ruler.unitSpacing,
                )
        ) {
            item("top spacer") {
                 Spacer(modifier = Modifier.padding(top = state.topPadding))
            }

            content()

            bottomBarSpacerItem()
        }
    }
}