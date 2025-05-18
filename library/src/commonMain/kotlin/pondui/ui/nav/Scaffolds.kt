package pondui.ui.nav

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pondui.ui.behavior.SlideIn
import pondui.ui.theme.Pond
import pondui.ui.theme.Spacing
import pondui.ui.theme.toColumnArrangement


@Composable
fun LazyScaffold(
    showBottomNav: Boolean = true,
    spacing: Spacing = Spacing.Tight,
    transition: EnterTransition = slideInVertically { it },
    content: LazyListScope.() -> Unit
) {
    val portal = LocalPortal.current

    LaunchedEffect(showBottomNav) {
        portal.setBottomBarIsVisible(showBottomNav)
    }

    SlideIn(enter = transition) {
        LazyColumn(
            verticalArrangement = spacing.toColumnArrangement()
        ) {
            item {
                TopBarSpacer()
            }

            content()

            item {
                BottomBarSpacer()
            }
        }
    }
}

@Composable
fun Scaffold(
    showBottomNav: Boolean = true,
    transition: EnterTransition = slideInVertically { it },
    verticalArrangement: Arrangement.Vertical = Pond.ruler.columnTight,
    content: @Composable ColumnScope.() -> Unit
) {
    val portal = LocalPortal.current

    LaunchedEffect(Unit) {
        portal.setBottomBarIsVisible(showBottomNav)
    }

    SlideIn(enter = transition) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Pond.ruler.columnTight
        ) {
            TopBarSpacer()

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = verticalArrangement
            ) {
                content()
            }

            BottomBarSpacer()
        }
    }
}

@Composable
fun TopBarSpacer() {
    val state by LocalPortal.current.state.collectAsState()
    val height = if (state.topBarIsVisible) portalTopBarHeight else 0.dp
    Spacer(
        modifier = Modifier.animateContentSize()
            .height(height)
    )
}

@Composable
fun BottomBarSpacer() {
    val state by LocalPortal.current.state.collectAsState()
    val height = if (state.bottomBarIsVisible) portalBottomBarHeight else 0.dp
    Spacer(
        modifier = Modifier.animateContentSize()
            .height(height)
    )
}