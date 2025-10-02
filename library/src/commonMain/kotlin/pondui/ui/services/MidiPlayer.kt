package pondui.ui.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import java.lang.Math.pow
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface MidiPlayer : AutoCloseable {
    suspend fun playSuspended(note: Int, duration: Duration = 1.seconds, velocity: Int = 100, channel: Int = 0)
    fun play(note: Int, duration: Duration = 1.seconds, velocity: Int = 100, channel: Int = 0)
    fun program(program: Int, channel: Int = 0) {} // no-op if unsupported
    override fun close() {}
}

fun midiNoteToHz(n: Int): Double = 440.0 * 2.0.pow((n - 69) / 12.0)

expect fun createMidiPlayer(scope: CoroutineScope): MidiPlayer

@Composable
fun rememberMidiPlayer(): MidiPlayer {
    val scope = rememberCoroutineScope()
    val midi = remember { createMidiPlayer(scope) }
    DisposableEffect(Unit) {
        onDispose {
            midi.close()
        }
    }
    return midi
}

fun MidiPlayer.playChord(
    notes: List<Int>,
    duration: Duration = 1.seconds,
    velocity: Int = 100,
    channel: Int = 0,
    program: Int? = null,
) {
    program?.let { program(it, channel) }
    notes.forEach { note ->
        play(note, duration, velocity, channel)
    }
}