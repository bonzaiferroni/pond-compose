package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.window.Dialog
import pondui.ui.behavior.Magic
import pondui.ui.behavior.HotKey
import pondui.ui.nav.LocalPortal
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideBookColors

@Composable
fun Cloud(
    isVisible: Boolean,
    toggle: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit) -> Unit,
) {
    CloudContent(
        isVisible = isVisible,
        onDismiss = toggle,
    ) {
        Magic(scale = .8f) {
            Box(
                modifier = modifier
                    .shadow(Pond.ruler.shadowElevation, shape = Pond.ruler.bigCorners)
                    .background(Pond.localColors.surface)
                    .padding(Pond.ruler.doublePadding)
            ) {
                content(toggle)
            }
        }
    }
}

@Composable
fun CloudContent(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    if (!isVisible) return

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        ProvideBookColors {
            content()
        }
    }
}

@Composable
fun TitleCloud (
    title: String,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val cloudPortal = LocalPortal.current.cloudPortalModel
    if (isVisible) HotKey(Key.Escape, onDismiss)

    cloudPortal.setDialogContent(title, isVisible, onDismiss) {
        content()
    }
}

@Composable
fun rememberCloud(
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit) -> Unit
): () -> Unit {
    var isVisible by remember { mutableStateOf(false) }

    val toggle = { isVisible = !isVisible }

    Cloud(
        isVisible = isVisible,
        toggle = toggle,
        modifier = modifier,
        content = content
    )

    return toggle
}