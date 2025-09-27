package pondui.ui.services

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
actual fun rememberTextSpeaker(
    locale: Locale,
    onDone: (String) -> Unit
): (String) -> Unit {
    val context = LocalContext.current
    val onDoneState by rememberUpdatedState(onDone)
    var ready by remember { mutableStateOf(false) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context, locale) {
        lateinit var engine: TextToSpeech

        val listener = TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                engine.language = locale
                engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onError(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        utteranceId?.let { onDoneState(it) }
                    }
                })
                engine.voice = engine.voices.first { it.name == "en-NG-language" }
                ready = true
            }
        }

        engine = TextToSpeech(context, listener)
        tts = engine

        onDispose {
            ready = false
            engine.stop()
            engine.shutdown()
            tts = null
        }
    }

    return remember {
        { text ->
            if (!ready) return@remember
            val tts = tts ?: return@remember
            val id = "utt-${System.currentTimeMillis()}"
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle(), id)
        }
    }
}

// best voices:
// en-gb-x-gba-network
// en-in-x-ene-local
// en-IN-language
// en-gb-x-rjs-network
// en-in-x-enc-local
// en-NG-language
//            val voice = tts.voices.filter { it.locale.language == "en" }.random()
//            println("chose ${voice.name}")
//            tts.voice = voice