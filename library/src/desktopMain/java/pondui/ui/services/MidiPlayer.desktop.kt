package pondui.ui.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pondui.ui.core.ModelState
import javax.sound.midi.*
import kotlin.time.Duration


class DesktopMidiPlayer(private val coroutineScope: CoroutineScope) : MidiPlayer {

    override val stateFlow = ModelState(MidiState())

    private var _sequencer: Sequencer? = null

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

    override fun changeProgram(program: Int, channel: Int) {
        if (programAt(channel) == program) return
        channelOf(channel).programChange(program.coerceIn(0,127))
    }

    override fun close() {
        synth.close()
        _sequencer?.close()
    }

    override fun programAt(channel: Int) = channelOf(channel).program

    override fun play(sequence: MidiSequence, channel: Int) {
        stateFlow.setValue { it.copy(isPlaying = true) }
        coroutineScope.launch(Dispatchers.IO) {
            val qnr = QUARTER_NOTE_RESOLUTION
            // Create a sequence (PPQ timing, resolution 480 ticks per quarter note)
            val seq = Sequence(Sequence.PPQ, qnr)
            val track: Track = seq.createTrack()

            val velocity = 100 // Note volume
            var tick = 0L // Start time
            val tickScale = 120 / (sequence.tempo ?: 120).toFloat()

            sequence.chords.forEach { chord ->
                val duration = (chord.duration * qnr * 4 * tickScale).toInt()
                chord.notes?.forEach { note ->
                    track.add(createNoteEvent(ShortMessage.NOTE_ON, channel, note, velocity, tick))
                    track.add(createNoteEvent(ShortMessage.NOTE_OFF, channel, note, 0, tick + duration))
                }
                tick += duration
            }

            // Open a sequencer and play
            val sequencer = getSequencer()
            sequencer.sequence = seq
            sequencer.start()

            // Let it play for a few seconds then close
            while(sequencer.isRunning) {
                delay(100)
            }
            sequencer.stop()
            stateFlow.setValue { it.copy(isPlaying = false) }
        }
    }

    override fun stop() {
        if (!stateFlow.value.isPlaying) return
        _sequencer?.stop()
        stateFlow.setValue { it.copy(isPlaying = false) }
    }

    private suspend fun getSequencer() = _sequencer ?:MidiSystem.getSequencer().also {
        _sequencer = it
        it.open()
        delay(200)
    }

    @Throws(Exception::class)
    private fun createNoteEvent(command: Int, channel: Int, note: Int, velocity: Int, tick: Long): MidiEvent {
        val message = ShortMessage()
        message.setMessage(command, channel, note, velocity)
        return MidiEvent(message, tick)
    }

    private fun channelOf(channel: Int) = synth.channels.getOrNull(channel) ?: error("channel not found: $channel")
}

actual fun createMidiPlayer(scope: CoroutineScope): MidiPlayer = DesktopMidiPlayer(scope)

private const val QUARTER_NOTE_RESOLUTION = 480