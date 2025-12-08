package pondui.ui.services

import kabinet.utils.toWav
import kotlinx.coroutines.delay
import java.io.ByteArrayInputStream
import java.io.Closeable
import java.io.File
import java.net.URI
import javax.sound.sampled.*
import kotlin.math.log10

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class WavePlayer {

    actual suspend fun play(url: String) {
        try {
            AudioSystem.getClip().use { clip ->
                val ais = if (url.startsWith("http")) {
                    AudioSystem.getAudioInputStream(URI.create(url).toURL())
                } else {
                    AudioSystem.getAudioInputStream(File(url).absoluteFile)
                }
                clip.open(ais)
                clip.play()
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    actual suspend fun play(bytes: ByteArray) {
        try {
            AudioSystem.getClip().use { clip ->
                clip.openBytes(bytes)
                clip.play()
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    actual suspend fun play(pcm: ShortArray, sampleRate: Int) {
        play(pcm.toWav(sampleRate))
    }

    actual fun readInfo(bytes: ByteArray): Int? {
        return try {
            AudioSystem.getClip().use { clip ->
                clip.openBytes(bytes)
                clip.lengthMillis
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    actual fun getClip(bytes: ByteArray): WaveClip {
        val clip = AudioSystem.getClip()
        clip.openBytes(bytes)
        return AudioSystemClip(clip)
    }

    actual fun getStream(sampleRate: Int): WaveStream {
        return AudioSystemStream(
            format = AudioFormat(
                /* encoding = */ AudioFormat.Encoding.PCM_SIGNED,
                /* sampleRate = */ sampleRate.toFloat(),
                /* sampleSizeInBits = */ 16,
                /* channels = */ 1,
                /* frameSize = */ 2,
                /* frameRate = */ sampleRate.toFloat(),
                /* bigEndian = */ false
            )
        )
    }
}

class AudioSystemClip(
    private val clip: Clip,
): WaveClip {
    override val length get() = clip.lengthMillis
    override val progress get() = clip.progressMillis
    override val isPlaying get() = clip.isRunning
    override suspend fun play(onProgress: suspend (Int) -> Unit) {
        clip.start()
        delay(100)
        while(clip.isRunning) {
            onProgress(clip.progressMillis)
            delay(100)
        }
    }
    override fun pause() = clip.stop()
    override fun close() = clip.close()
    override fun reset() { clip.framePosition = 0 }
}

fun Clip.setVolume(volume: Float) {
    val gain = 20 * log10(volume.coerceIn(0.0001f, 1f))
    val control = getControl(FloatControl.Type.MASTER_GAIN) as? FloatControl ?: return
    control.value = gain
}

fun Clip.openBytes(bytes: ByteArray) {
    val inputStream = ByteArrayInputStream(bytes)
    val audioStream = AudioSystem.getAudioInputStream(inputStream)
    open(audioStream)
}

suspend fun Clip.play() {
    start()
    delay(lengthMillis.toLong())
    while (isRunning) {
        delay(100)
    }
}

val Clip.lengthMillis get() = (microsecondLength / 1_000).toInt()
val Clip.progressMillis get() = (microsecondPosition / 1_000).toInt()

class AudioSystemStream(
    format: AudioFormat
): WaveStream {

    private val line: SourceDataLine =
        AudioSystem.getSourceDataLine(format).apply {
            open(format)
            start()
        }

    @Volatile
    private var closed = false

    override fun write(chunk: ByteArray, length: Int) {
        if (closed) return
        line.write(chunk, 0, length)
    }

    override fun close() {
        if (closed) return
        closed = true
        line.drain()
        line.stop()
        line.close()
    }
}