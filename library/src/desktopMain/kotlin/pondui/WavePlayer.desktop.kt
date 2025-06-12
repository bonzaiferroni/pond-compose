package pondui

import kotlinx.coroutines.delay
import java.net.URI
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.FloatControl
import kotlin.math.log10

actual class WavePlayer {
    private val clip = AudioSystem.getClip()

    actual suspend fun play(url: String) {
        if (clip.isRunning) {
            for (i in 1..10) {
                clip.setVolume((10 - i).toFloat())
                delay(100)
            }
        }
        val stream = AudioSystem.getAudioInputStream(URI.create(url).toURL())
        clip.open(stream)
        clip.start()
    }
}

fun Clip.setVolume(volume: Float) {
    val gain = 20 * log10(volume.coerceIn(0.0001f, 1f))
    val control = getControl(FloatControl.Type.MASTER_GAIN) as? FloatControl ?: return
    control.value = gain
}