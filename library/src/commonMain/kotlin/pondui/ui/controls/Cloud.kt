package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import pondui.ui.behavior.FadeIn
import pondui.ui.behavior.HotKey
import pondui.ui.nav.LocalPortal
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
        FadeIn {
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
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val portal = LocalPortal.current
    if (isVisible) HotKey(Key.Escape, onDismiss)

    portal.setDialogContent(title, isVisible, onDismiss) {
        ProvideBookColors {
            FadeIn(offsetX = 60) {
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
    }
}