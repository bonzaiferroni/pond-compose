package pondui.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

@Composable
fun Modifier.onHotKey(
    key: Key,
    eventType: KeyEventType = KeyEventType.KeyDown,
    action: () -> Unit,
) = onPreviewKeyEvent { event ->
    if (event.key == key && event.type == eventType) {
        action()
        true
    } else {
        false
    }
}

@Composable
fun Modifier.onHotKeyConsume(
    key: Key,
    eventType: KeyEventType = KeyEventType.KeyDown,
    action: () -> Boolean,
) = onPreviewKeyEvent { event ->
    if (event.key == key && event.type == eventType) {
        action()
    } else {
        false
    }
}