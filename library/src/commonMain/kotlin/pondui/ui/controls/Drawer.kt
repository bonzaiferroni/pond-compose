package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.modifiers.Magic
import pondui.ui.modifiers.ifTrue

@Composable
fun Drawer(
    isOpen: Boolean,
    openHeight: Dp? = null,
    closedHeight: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.animateContentSize()
            .ifTrue(!isOpen || openHeight != null) {
                heightIn(max = if (isOpen) openHeight!! else closedHeight)
            }
    ) {
        Magic(isOpen, scale = .8f) {
            content()
        }
    }
}