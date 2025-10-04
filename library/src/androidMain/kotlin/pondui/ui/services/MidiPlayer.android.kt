package pondui.ui.services

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import pondui.ui.core.ModelState
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.sin
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration

class AndroidMidiPlayer(private val coroutineScope: CoroutineScope) : MidiPlayer {
    private val state = ModelState(MidiState())
    override val stateFlow = state

    private val sampleRate = 48000
    private val track = AudioTrack(
        AudioManager.STREAM_MUSIC,
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ),
        AudioTrack.MODE_STREAM
    )
    private val running = AtomicBoolean(true)
    private val voices = ConcurrentHashMap<Int, Voice>() // key = note
    private val renderThread = thread(start = true, name = "MidiSineSynth") { renderLoop() }

    private data class Voice(var phase: Double, var incr: Double, var gain: Double)

    override suspend fun playSuspended(note: Int, duration: Duration, velocity: Int, channel: Int) {
        noteOn(note, velocity, channel)
        delay(duration)
        noteOff(note, channel)
    }

    override fun play(note: Int, duration: Duration, velocity: Int, channel: Int) {
        coroutineScope.launch {
            playSuspended(note, duration, velocity, channel)
        }
    }

    override fun programAt(channel: Int): Int {
        TODO("Not yet implemented")
    }

    override fun play(sequence: MidiSequence, channel: Int) {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    private fun noteOn(note: Int, velocity: Int, channel: Int) {
        val hz = midiNoteToHz(note)
        val incr = 2.0 * PI * hz / sampleRate
        val gain = (velocity.coerceIn(0,127) / 127.0) * 0.3 // gentle
        voices[note] = Voice(phase = 0.0, incr = incr, gain = gain)
        if (track.playState != AudioTrack.PLAYSTATE_PLAYING) track.play()
    }

    private fun noteOff(note: Int, channel: Int) {
        voices.remove(note)
        if (voices.isEmpty()) track.pause()
    }

    private fun renderLoop() {
        val buf = ShortArray(1024)
        while (running.get()) {
            if (voices.isEmpty()) {
                Thread.sleep(5)
                continue
            }
            for (i in buf.indices) {
                var s = 0.0
                // simple mix of sines
                voices.values.forEach { v ->
                    s += sin(v.phase) * v.gain
                    v.phase += v.incr
                    if (v.phase > 2 * PI) v.phase -= 2 * PI
                }
                val clamp = (s.coerceIn(-1.0, 1.0) * 32767.0).toInt()
                buf[i] = clamp.toShort()
            }
            track.write(buf, 0, buf.size)
        }
    }

    override fun close() {
        running.set(false)
        renderThread.join()
        track.stop()
        track.release()
        voices.clear()
    }
}

actual fun createMidiPlayer(scope: CoroutineScope): MidiPlayer = AndroidMidiPlayer(scope)