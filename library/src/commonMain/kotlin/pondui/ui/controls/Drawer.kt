package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.behavior.Magic

@Composable
fun Drawer(
    isOpen: Boolean,
    height: Dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.animateContentSize()
            .height(if (isOpen) height else 0.dp)
    ) {
        Magic(isOpen, scale = .8f) {
            content()
        }
    }
}