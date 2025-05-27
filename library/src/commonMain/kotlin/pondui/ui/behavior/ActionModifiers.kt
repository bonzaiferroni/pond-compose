package pondui.ui.behavior

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import pondui.ui.nav.LocalKeyCaster

@Composable
fun HotKey(key: Key, onPress: () -> Unit) {
    val portal = LocalKeyCaster.current
    DisposableEffect(Unit) {
        val func: (KeyEvent) -> Boolean = { event ->
            val isPressed = event.key == key
            if (isPressed) onPress()
            isPressed
        }
        portal.keypresses.register(func)
        onDispose {
            portal.keypresses.unregister(func)
        }
    }
}