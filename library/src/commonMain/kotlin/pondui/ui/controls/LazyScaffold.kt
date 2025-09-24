package pondui.ui.controls

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pondui.ui.modifiers.SlideIn
import pondui.ui.nav.LocalPortal
import pondui.ui.theme.Pond
import pondui.ui.theme.Spacing
import pondui.ui.theme.toColumnArrangement

@Composable
fun LazyScaffold(
    showBottomNav: Boolean = true,
    spacing: Spacing = Spacing.Unit,
    transition: EnterTransition = slideInVertically { it },
    contentPadding: PaddingValues = PaddingValues(
        start = Pond.ruler.unitSpacing,
        end = Pond.ruler.unitSpacing,
        top = 0.dp,
        bottom = 0.dp,
    ),
    content: LazyListScope.() -> Unit
) {
    val portal = LocalPortal.current

    LaunchedEffect(showBottomNav) {
        portal.setBottomBarIsVisible(showBottomNav)
    }

    SlideIn(enter = transition) {
        LazyColumn(
            verticalArrangement = spacing.toColumnArrangement(),
            modifier = Modifier.padding(contentPadding)
        ) {
            topBarSpacerItem()

            content()

            bottomBarSpacerItem()
        }
    }
}

fun LazyListScope.topBarSpacerItem() = item("TopBarSpacer") {
    TopBarSpacer()
}

fun LazyListScope.bottomBarSpacerItem() = item("BottomBarSpacer") {
    BottomBarSpacer()
}

fun LazyListScope.provideTopPadding(padding: PaddingValues) = item("header_padding") {
    Box(modifier = Modifier.height(padding.calculateTopPadding()))
}

fun LazyListScope.provideBottomPadding(padding: PaddingValues) = item("footer_padding") {
    Box(modifier = Modifier.height(padding.calculateBottomPadding()))
}