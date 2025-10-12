package pondui.ui.services

import androidx.compose.runtime.Composable
import pondui.ui.controls.Text


enum class PcmFormat { S16LE }

data class AudioSpec(
    val sampleRate: Int = 44100,
    val channels: Int = 1,
    val format: PcmFormat = PcmFormat.S16LE,
    val framesPerChunk: Int = 1024
)

typealias Pcm16 = ShortArray

interface MicStream {
    fun start()
    fun stop()
}

expect fun createMicStream(spec: AudioSpec, onChunk: (Pcm16, Int) -> Unit): MicStream

@Composable
expect fun MicPermissionRequester(
    deniedContent: @Composable () -> Unit = { Text("Permission denied") },
    grantedContent: @Composable () -> Unit
)