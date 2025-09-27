package pondui.ui.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

actual class SpeechToText private constructor(
    private val appContext: Context
) {
    private val recognizer: SpeechRecognizer =
        SpeechRecognizer.createSpeechRecognizer(appContext)

    private val _isAvailable = MutableStateFlow(
        SpeechRecognizer.isRecognitionAvailable(appContext)
    )
    actual val isAvailable: StateFlow<Boolean> = _isAvailable

    private val _events = MutableSharedFlow<SttEvent>(
        replay = 0,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    actual val events: SharedFlow<SttEvent> = _events

    private var started = false

    init {
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _events.tryEmit(SttEvent.Ready)
            }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                _events.tryEmit(SttEvent.EndOfUtterance)
            }
            override fun onError(error: Int) {
                _events.tryEmit(SttEvent.Error(error, errorToMsg(error)))
                started = false
            }
            override fun onResults(results: Bundle) {
                val best = results.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )?.firstOrNull().orEmpty()
                if (best.isNotEmpty()) _events.tryEmit(SttEvent.Final(best))
                started = false
            }
            override fun onPartialResults(partialResults: Bundle) {
                val text = partialResults.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )?.firstOrNull().orEmpty()
                if (text.isNotEmpty()) _events.tryEmit(SttEvent.Partial(text))
            }
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    actual suspend fun start(config: SttConfig) {
        checkRecordAudioPermission(appContext)
        if (started) return
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, config.partials)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, config.preferOffline)
            config.languageTag?.let {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, it)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, it)
            }
        }
        recognizer.startListening(intent)
        started = true
    }

    actual fun stop() {
        recognizer.stopListening()
    }

    actual fun cancel() {
        recognizer.cancel()
        started = false
    }

    actual fun release() {
        recognizer.destroy()
    }

    private fun errorToMsg(code: Int): String = when (code) {
        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
        SpeechRecognizer.ERROR_CLIENT -> "Client error"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
        SpeechRecognizer.ERROR_NETWORK -> "Network error"
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
        SpeechRecognizer.ERROR_NO_MATCH -> "No match"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
        SpeechRecognizer.ERROR_SERVER -> "Server error"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout"
        else -> "Unknown error ($code)"
    }

    companion object {
        fun create(context: Context): SpeechToText = SpeechToText(context.applicationContext)
    }
}

@Composable
actual fun rememberSpeechToText(): SpeechToText {
    val ctx = LocalContext.current
    // Simple remember; tie lifecycle to composition owner
    return remember(ctx) { SpeechToText.create(ctx) }
}

// --- Permissions (minimal guard) ---
private fun checkRecordAudioPermission(context: Context) {
    // Keep it blunt: throw if missing; app should request before starting.
    val granted = context.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) ==
            android.content.pm.PackageManager.PERMISSION_GRANTED
    if (!granted) error("RECORD_AUDIO permission not granted")
}