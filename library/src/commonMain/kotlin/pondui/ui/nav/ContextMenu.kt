package pondui.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import pondui.ui.modifiers.Magic

@Composable
fun ContextMenu(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    Magic(
        isVisible = isVisible,
    ) {
        Popup(
            onDismissRequest = onDismiss,
        ) {
            Magic(isVisible, scale = .9f, offsetY = 5.dp) {
                content()
            }
        }
    }
}