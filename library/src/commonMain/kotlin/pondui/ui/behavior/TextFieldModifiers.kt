package pondui.ui.behavior

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

@Composable
fun Modifier.onEnterPressed(block: () -> Unit) = this.onPreviewKeyEvent { event ->
    val enterPressed = event.type == KeyEventType.KeyDown && event.key == Key.Enter && !event.isShiftPressed
    if (enterPressed) block()
    enterPressed
}