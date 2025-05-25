package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.window.Dialog
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors

@Composable
fun Cloud(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CloudContent(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        Box(
            modifier = modifier
                .shadow(Pond.ruler.shadowElevation, shape = Pond.ruler.bigCorners)
                .background(Pond.localColors.surface)
                .padding(Pond.ruler.doublePadding)
        ) {
            content()
        }
    }
}

@Composable
fun CloudContent(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
        ) {
            ProvideBookColors {
                content()
            }
        }
    }
}