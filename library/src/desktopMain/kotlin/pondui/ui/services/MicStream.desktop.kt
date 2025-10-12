package pondui.ui.services

import androidx.compose.runtime.Composable
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.concurrent.thread

actual fun createMicStream(spec: AudioSpec, onChunk: (Pcm16, Int) -> Unit): MicStream {
    require(spec.format == PcmFormat.S16LE) { "Only S16LE supported" }

    val jFormat = AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        spec.sampleRate.toFloat(),
        16,
        spec.channels,
        2 * spec.channels,
        spec.sampleRate.toFloat(),
        false // little-endian
    )
    val info = DataLine.Info(TargetDataLine::class.java, jFormat)
    val line = AudioSystem.getLine(info) as TargetDataLine
    line.open(jFormat)

    return object : MicStream {
        @Volatile private var running = false
        private var t: Thread? = null

        override fun start() {
            if (running) return
            running = true
            line.start()
            t = thread(isDaemon = true, name = "MicStream-Desktop") {
                val bytesPerFrame = 2 * spec.channels
                val byteBuf = ByteArray(spec.framesPerChunk * bytesPerFrame)
                val shortBuf = ShortArray(spec.framesPerChunk * spec.channels)
                while (running) {
                    var got = 0
                    while (running && got < byteBuf.size) {
                        val n = line.read(byteBuf, got, byteBuf.size - got)
                        if (n > 0) got += n else break
                    }
                    if (got > 0) {
                        val frames = got / bytesPerFrame
                        // bytes -> shorts (LE)
                        var si = 0
                        var bi = 0
                        val samples = frames * spec.channels
                        while (si < samples) {
                            val lo = byteBuf[bi].toInt() and 0xFF
                            val hi = byteBuf[bi + 1].toInt()
                            shortBuf[si] = ((hi shl 8) or lo).toShort()
                            si++
                            bi += 2
                        }
                        onChunk(shortBuf, frames)
                    }
                }
            }
        }

        override fun stop() {
            running = false
            t?.join()
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