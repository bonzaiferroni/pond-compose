package pondui.ui.services

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.*

data class SttConfig(
    val languageTag: String? = null,   // e.g., "en-US"
    val partials: Boolean = true,
    val preferOffline: Boolean = false
)

sealed class SttEvent {
    data object Ready : SttEvent()
    data object EndOfUtterance : SttEvent()
    data class Partial(val text: String) : SttEvent()
    data class Final(val text: String) : SttEvent()
    data class Error(val code: Int, val message: String) : SttEvent()
}

expect class SpeechToText {
    val isAvailable: StateFlow<Boolean>
    val events: SharedFlow<SttEvent>

    suspend fun start(config: SttConfig = SttConfig())
    fun stop()      // graceful end (lets ASR finalize)
    fun cancel()    // abort immediately
    fun release()
}

@Composable
expect fun rememberSpeechToText(): SpeechToText