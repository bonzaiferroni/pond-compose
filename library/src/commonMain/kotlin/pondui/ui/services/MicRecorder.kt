package pondui.ui.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kabinet.utils.ShortBuffer
import kotlinx.coroutines.flow.StateFlow
import pondui.ui.core.ModelState

interface MicRecorder {
    val stateFlow: StateFlow<MicRecorderState>

    fun start()
    fun stop()
}

@Composable
fun rememberMicRecorder(
    spec: AudioSpec = AudioSpec(),
    onFinished: (ShortArray) -> Unit
): MicRecorder {
    val buffer = remember { ShortBuffer() }
    val scope = rememberCoroutineScope()
    val stream = remember { createMicStream(scope, spec) { bytes, frames ->
        buffer.append(bytes.toPcmShortArray())
    } }

    val recorder = remember { object: MicRecorder {
        val state = ModelState(MicRecorderState())
        override val stateFlow: StateFlow<MicRecorderState> = state

        override fun start() {
            buffer.clear()
            stream.start()
            state.setValue { it.copy(isRecording = true) }
        }

        override fun stop() {
            state.setValue { it.copy(isRecording = false) }
            stream.stop()
            onFinished(buffer.toArray())
        }
    }}
    return recorder
}

data class MicRecorderState(
    val isRecording: Boolean = false
)