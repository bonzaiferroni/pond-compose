package pondui.ui.controls

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pondui.ui.modifiers.SlideIn
import pondui.ui.modifiers.artBackground
import pondui.ui.nav.LocalPortal
import pondui.ui.nav.portalBottomBarHeight
import pondui.ui.nav.portalTopBarHeight
import pondui.ui.theme.Pond

@Composable
fun Scaffold(
    showBottomNav: Boolean = true,
    transition: EnterTransition = slideInVertically { it },
    verticalArrangement: Arrangement.Vertical = Pond.ruler.columnUnit,
    contentPadding: PaddingValues = PaddingValues(
        start = Pond.ruler.unitSpacing,
        end = Pond.ruler.unitSpacing,
        top = 0.dp,
        bottom = 0.dp,
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    val portal = LocalPortal.current

    LaunchedEffect(Unit) {
        portal.setBottomBarIsVisible(showBottomNav)
    }

    SlideIn(enter = transition) {
        Column(
            modifier = Modifier.fillMaxSize()
                .artBackground(),
            verticalArrangement = Pond.ruler.columnUnit
        ) {
            TopBarSpacer()

            Column(
                verticalArrangement = verticalArrangement,
                modifier = Modifier.weight(1f)
                    .padding(contentPadding)
            ) {
                content()
            }

            BottomBarSpacer()
        }
    }
}

@Composable
fun TopBarSpacer() {
    val state by LocalPortal.current.stateFlow.collectAsState()
    val height = if (state.topBarIsVisible) portalTopBarHeight else 0.dp
    Spacer(
        modifier = Modifier.animateContentSize()
            .height(height)
    )
}

@Composable
fun BottomBarSpacer() {
    val state by LocalPortal.current.stateFlow.collectAsState()
    val height = if (state.bottomBarIsVisible) portalBottomBarHeight else 0.dp
    Spacer(
        modifier = Modifier.animateContentSize()
            .height(height)
    )
}