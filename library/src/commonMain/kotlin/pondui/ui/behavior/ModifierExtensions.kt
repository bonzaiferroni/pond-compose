package pondui.ui.behavior

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun <V> Modifier.ifNotNull(
    value: V?,
    block: @Composable Modifier.(V) -> Modifier
): Modifier {
    if (value != null) return this.block(value)
    return this
}

@Composable
fun Modifier.ifTrue(
    value: Boolean,
    block: @Composable Modifier.() -> Modifier
): Modifier {
    return when {
        value -> this.block()
        else -> this
    }
}

@Composable
fun Modifier.changeFocusWithTab(
    focusManager: FocusManager = LocalFocusManager.current
) = this.onPreviewKeyEvent {
    if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
        if (it.isShiftPressed) {
            focusManager.moveFocus(FocusDirection.Previous)
        } else {
            focusManager.moveFocus(FocusDirection.Next)
        }
        true
    } else {
        false
    }
}