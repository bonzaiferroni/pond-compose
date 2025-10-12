package pondui.ui.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine

actual class PcmStream actual constructor(
    sampleRate: Int,
    channels: Int,
    capacity: Int
) : AutoCloseable {

    private val format = AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        sampleRate.toFloat(),
        16,
        channels,
        channels * 2,
        sampleRate.toFloat(),
        false
    )

    private val info = DataLine.Info(SourceDataLine::class.java, format)
    private val line = AudioSystem.getLine(info) as SourceDataLine

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val inbox = Channel<ByteArray>(capacity)
    private var job: Job? = null

    actual fun start() {
        if (job != null) return
        line.open(format)
        line.start()
        job = scope.launch {
            try {
                for (chunk in inbox) {
                    var off = 0
                    while (off < chunk.size) {
                        off += line.write(chunk, off, chunk.size - off)
                    }
                }
                println("draining line")
                line.drain()
            } finally {
                println("closing line")
                line.stop()
                line.close()
            }
        }
    }

    actual suspend fun send(chunk: ByteArray) {
        inbox.send(chunk) // suspends if buffer full; preserves order
    }

    actual fun trySend(chunk: ByteArray): Boolean =
        inbox.trySend(chunk).isSuccess

    actual suspend fun finish() {
        inbox.close()
        job?.join()
    }

    override fun close() {
        inbox.close()
        runBlocking { job?.cancelAndJoin() }
        if (line.isOpen) {
            line.stop()
            line.close()
        }
    }
}