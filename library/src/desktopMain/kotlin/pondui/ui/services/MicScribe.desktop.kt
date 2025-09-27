package pondui.ui.services

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

actual class SpeechToText {
    actual val isAvailable: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    actual val events: SharedFlow<SttEvent>
        get() = TODO("Not yet implemented")

    actual suspend fun start(config: SttConfig) {
    }

    actual fun stop() {
    }

    actual fun cancel() {
    }

    actual fun release() {
    }
}

@Composable
actual fun rememberSpeechToText(): SpeechToText {
    TODO("Not yet implemented")
}