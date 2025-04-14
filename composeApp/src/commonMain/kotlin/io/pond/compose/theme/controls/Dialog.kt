package newsref.app.pond.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import newsref.app.pond.theme.Pond
import newsref.app.pond.theme.ProvideBookColors

@Composable
fun FloatyBox(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    FloatyContent(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        Box(
            modifier = Modifier
                .sizeIn(minWidth = 200.dp, maxWidth = 400.dp, minHeight = 100.dp)
                .shadow(Pond.ruler.shadowElevation, shape = Pond.ruler.rounded)
                .background(Pond.localColors.surface)
                .padding(Pond.ruler.halfPadding)
        ) {
            content()
        }
    }
}

@Composable
fun FloatyContent(
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