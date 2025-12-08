package pondui.ui.services

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import pondui.ui.controls.Text

enum class PcmFormat { S16LE }

data class AudioSpec(
    val sampleRate: Int = 44100,
    val channels: Int = 1,
    val format: PcmFormat = PcmFormat.S16LE,
    val framesPerChunk: Int = 1024
)

interface MicStream {
    fun start()
    fun stop()
}

expect fun createMicStream(
    scope: CoroutineScope,
    spec: AudioSpec,
    onChunk: (ByteArray, Int) -> Unit
): MicStream

@Composable
expect fun MicPermissionRequester(
    deniedContent: @Composable () -> Unit = { Text("Permission denied") },
    grantedContent: @Composable () -> Unit
)

fun ByteArray.toPcmShortArray(): ShortArray {
    val out = ShortArray(size / 2)
    var i = 0
    var j = 0
    while (j < out.size) {
        val lo = this[i].toInt() and 0xFF
        val hi = this[i + 1].toInt()
        out[j] = ((hi shl 8) or lo).toShort()
        i += 2
        j += 1
    }
    return out
}

fun ShortArray.toPcmByteArray(): ByteArray {
    val out = ByteArray(size * 2)
    var i = 0
    var j = 0
    while (i < size) {
        val v = this[i].toInt()
        out[j] = (v and 0xFF).toByte()          // lo
        out[j + 1] = ((v ushr 8) and 0xFF).toByte() // hi
        i += 1
        j += 2
    }
    return out
}