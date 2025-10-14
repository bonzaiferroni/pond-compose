package pondui.ui.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import compose.icons.TablerIcons
import compose.icons.tablericons.PlayerPlay
import compose.icons.tablericons.PlayerStop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import pondui.ui.controls.IconButton
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface MidiPlayer : AutoCloseable {
    val stateFlow: StateFlow<MidiState>

    suspend fun playSuspended(note: Int, duration: Duration = 1.seconds, velocity: Int = 100, channel: Int = 0)
    fun play(note: Int, duration: Duration = 1.seconds, velocity: Int = 100, channel: Int = 0)
    fun changeProgram(program: Int, channel: Int = 0) {} // no-op if unsupported
    fun programAt(channel: Int): Int
    fun play(sequence: MidiSequence, channel: Int = 0)
    fun stop()
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
    if (program != null) changeProgram(program, channel)
    notes.forEach { note ->
        play(note, duration, velocity, channel)
    }
}

data class MidiSequence(
    val tempo: Int? = null,
    val chords: List<MidiChord>,
)

data class MidiChord(
    val beats: Int,
    val notes: List<Int>?
)

data class MidiState(
    val isPlaying: Boolean = false
)

@Composable
fun MidiPlayer.MiniPlayer(
    provideSequence: () -> MidiSequence?
) {
    val state by stateFlow.collectAsState()
    if (state.isPlaying) {
        IconButton(TablerIcons.PlayerStop) {
            stop()
        }
    } else {
        IconButton(TablerIcons.PlayerPlay) {
            val sequence = provideSequence() ?: return@IconButton
            play(sequence)
        }
    }
}