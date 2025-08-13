package pondui.ui.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.portalBottomBarHeight
import pondui.ui.nav.portalTopBarHeight
import pondui.ui.theme.Pond
import pondui.utils.darken

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun DrawerScaffold(
    drawerContent: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val state by LocalPortal.current.stateFlow.collectAsState()
    val unitSpacing = Pond.ruler.unitSpacing
    val bottomPadding = if (state.bottomBarIsVisible) portalBottomBarHeight else 0.dp
    var topPadding by remember { mutableStateOf(0.dp)}
    val density = LocalDensity.current
    val hazeState = remember { HazeState() }

    Box {
        Box(
            modifier = Modifier.hazeSource(state = hazeState)
                .fillMaxSize()
                .padding(horizontal = unitSpacing)
        ) {
            content(PaddingValues(
                top = topPadding,
                bottom = bottomPadding
            ))
        }

        Column(
            gap = 1,
            modifier = Modifier.onGloballyPositioned { topPadding = with (density) { it.size.height.toDp() } }
                .clip(RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = Pond.ruler.bigCorner,
                    bottomEnd = Pond.ruler.bigCorner
                ))
                .hazeEffect(state = hazeState, style = HazeMaterials.thin(Pond.colors.void.darken(.1f)))
                .padding(
                    top = portalTopBarHeight + unitSpacing,
                    start = unitSpacing,
                    end = unitSpacing,
                    bottom = unitSpacing,
                )
        ) {
            drawerContent()
        }
    }
}