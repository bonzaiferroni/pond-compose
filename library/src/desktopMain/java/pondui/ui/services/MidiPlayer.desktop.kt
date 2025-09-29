package pondui.ui.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.sound.midi.MidiSystem
import javax.sound.midi.Synthesizer
import kotlin.time.Duration

class DesktopMidiPlayer(private val coroutineScope: CoroutineScope) : MidiPlayer {
    private val synth: Synthesizer = MidiSystem.getSynthesizer().apply {
        open()
        channels.getOrNull(0)?.programChange(0)
    }

    override suspend fun playSuspended(note: Int, duration: Duration, velocity: Int, channel: Int) {
        channelOf(channel).noteOn(note, velocity)
        delay(duration)
        channelOf(channel).noteOff(note)
    }

    override fun play(note: Int, duration: Duration, velocity: Int, channel: Int) {
        coroutineScope.launch {
            playSuspended(note, duration, velocity, channel)
        }
    }

    override fun program(program: Int, channel: Int) {
        channelOf(channel).programChange(program.coerceIn(0,127))
    }

    override fun close() { synth.close() }

    private fun channelOf(channel: Int) = synth.channels.getOrNull(channel) ?: error("channel not found: $channel")
}

actual fun createMidiPlayer(scope: CoroutineScope): MidiPlayer = DesktopMidiPlayer(scope)