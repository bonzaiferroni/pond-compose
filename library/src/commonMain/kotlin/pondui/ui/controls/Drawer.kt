package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.modifiers.Magic
import pondui.ui.modifiers.ifTrue
import pondui.ui.modifiers.pad
import pondui.ui.theme.Pond

@Composable
fun Drawer(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    openHeight: Dp? = null,
    closedHeight: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.animateContentSize()
            .ifTrue(!isOpen || openHeight != null) {
                heightIn(max = if (isOpen) openHeight!! else closedHeight)
            }
            .pad(1)
    ) {
        content()
    }
}

@Composable
fun Drawer(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    headerContent: @Composable () -> Unit,
    color: Color = Pond.colors.secondary.copy(.5f),
    openHeight: Dp? = null,
    closedHeight: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    var drawerOpen by remember(isOpen) { mutableStateOf(isOpen) }
    Column(
        gap = 1,
        modifier = Modifier.clip(Pond.ruler.unitCorners)
            .background(Pond.localColors.sectionSurface)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .clip(Pond.ruler.unitCorners)
                .background(color)
                .actionable { drawerOpen = !drawerOpen }
                .pad(2)
        ) {
            headerContent()
        }
        Drawer(
            isOpen = drawerOpen,
            openHeight = openHeight,
            closedHeight = closedHeight,
            content = content
        )
    }
}