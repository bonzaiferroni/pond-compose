package pondui.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.type
import pondui.utils.Broadcaster

class KeyCaster {
    val keypresses = Broadcaster<KeyEvent>()

    fun keyEvent(event: KeyEvent): Boolean {
        if (event.type != KeyEventType.KeyDown) return false
        return keypresses.broadcast(event)
    }
}

// .onPreviewKeyEvent(viewModel::keyEvent)

val LocalKeyCaster = staticCompositionLocalOf<KeyCaster> {
    error("No keycaster provided")
}