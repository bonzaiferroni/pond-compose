package pondui.ui.services

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.concurrent.thread

actual fun createMicStream(
    scope: CoroutineScope,
    spec: AudioSpec,
    onChunk: (ByteArray, Int) -> Unit
): MicStream {
    require(spec.format == PcmFormat.S16LE) { "Only S16LE supported" }

    val jFormat = AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        spec.sampleRate.toFloat(),
        16,
        spec.channels,
        2 * spec.channels,
        spec.sampleRate.toFloat(),
        false
    )
    val info = DataLine.Info(TargetDataLine::class.java, jFormat)
    val line = AudioSystem.getLine(info) as TargetDataLine
    line.open(jFormat)

    return object : MicStream {
        private var job: Job? = null

        override fun start() {
            if (job != null) return
            line.start()
            val bytesPerFrame = 2 * spec.channels
            val byteBuf = ByteArray(spec.framesPerChunk * bytesPerFrame)

            job = scope.launch(Dispatchers.IO) {
                while (isActive) {
                    var got = 0
                    while (isActive && got < byteBuf.size) {
                        val n = line.read(byteBuf, got, byteBuf.size - got)
                        if (n > 0) got += n else break
                    }
                    if (got > 0) {
                        val frames = got / bytesPerFrame
                        if (frames > 0) {
                            val usedBytes = frames * bytesPerFrame
                            val chunk = byteBuf.copyOf(usedBytes)
                            onChunk(chunk, frames)
                        }
                    }
                }
            }
        }

        override fun stop() {
            job?.cancel()
            runBlocking {
                job?.join()
            }
            job = null
            line.stop()
            line.close()
        }
    }
}

@Composable
actual fun MicPermissionRequester(
    deniedContent: @Composable () -> Unit,
    grantedContent: @Composable () -> Unit
) {
    grantedContent()
}