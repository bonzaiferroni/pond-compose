package pondui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URI
import javax.sound.sampled.*
import kotlin.math.log10

actual class WavePlayer {
    private val clip = AudioSystem.getClip()
    private var job: Job? = null

    init {
        clip.addLineListener { ev ->
            if (ev.type == LineEvent.Type.STOP) {
                println("Stopped at frame ${clip.framePosition} of ${clip.frameLength}")
            }
        }
    }

    actual fun play(url: String) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                if (clip.isOpen) clip.close()
                // val bis = URI.create(url).toURL().openStream().buffered()
                val ais = if (url.startsWith("http")) {
                    AudioSystem.getAudioInputStream(URI.create(url).toURL())
                } else {
                    AudioSystem.getAudioInputStream(File(url).absoluteFile)
                }
                clip.framePosition = 0;
                clip.open(ais)
                val frames     = clip.frameLength                     // total sample-frames
                val frameRate  = clip.format.frameRate                // frames per second
                val durationS  = frames / frameRate                   // in seconds
                val durationMs = (durationS * 1000).toLong()          // in ms

                println("Clip expects: $durationS s ($durationMs ms), frames: $frames, frameRate: $frameRate")
//                clip.start()
                clip.loop(1)
                delay(durationMs)
                clip.stop()
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}
//    public actual override fun powerUp() {
//        startMixer()
//    }
//
//    private fun play(url: String) {
//
//    }
//
//    private fun startMixer() {
//        val clip = AudioSystem.getClip()
//        CoroutineScope(Dispatchers.Default).launch {
//            while (true) {
//                val result = mixer.receiveCatching()
//                when (val name = result.getOrNull()) {
//                    null -> break
//                    else -> {
//                        audioInputStreams[name]?.let {
//                            clip.open(it)
//                            clip.start()
//                        }
//                    }
//                }
//                delay(10)
//            }
//        }
//    }
//
//    public actual override fun powerDown() {
//        audioInputStreams.clear()
//        soundBytes.clear()
//    }
//}

fun Clip.setVolume(volume: Float) {
    val gain = 20 * log10(volume.coerceIn(0.0001f, 1f))
    val control = getControl(FloatControl.Type.MASTER_GAIN) as? FloatControl ?: return
    control.value = gain
}

