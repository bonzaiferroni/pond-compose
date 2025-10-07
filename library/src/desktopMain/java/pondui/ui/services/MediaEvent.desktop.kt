package pondui.ui.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import java.awt.KeyEventDispatcher
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent

@Composable
actual fun MediaEventEffect(onEvent: (MediaEvent) -> Unit) {
    DisposableEffect(Unit) {
        val dispatcher = KeyEventDispatcher { e ->
            if (e.id == KeyEvent.KEY_PRESSED) {
                when (e.keyCode) {
                    KeyEvent.VK_NUMPAD5 -> { onEvent(MediaEvent.PlayPause); true }
                    KeyEvent.VK_NUMPAD6 -> { onEvent(MediaEvent.Next); true }
                    KeyEvent.VK_NUMPAD4 -> { onEvent(MediaEvent.Previous); true }
                    else -> false
                }
            } else false
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher)
        onDispose {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher)
        }
    }
}