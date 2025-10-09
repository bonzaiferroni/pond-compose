package pondui.ui.services

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.net.HttpURLConnection
import java.net.URL
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.roundToInt

actual class WavePlayer {

    actual suspend fun play(url: String) {
        val bytes = fetch(url)
        getClip(bytes).use { it.play() }
    }

    actual suspend fun play(bytes: ByteArray) {
        getClip(bytes).use { it.play() }
    }

    actual fun getClip(bytes: ByteArray): WaveClip {
        val info = parseWav(bytes) ?: error("Unsupported/invalid WAV")
        require(info.encoding == AudioFormat.ENCODING_PCM_16BIT) { "Only PCM 16-bit supported" }
        require(info.channels == 1 || info.channels == 2) { "Only mono/stereo supported" }

        val channelMask = if (info.channels == 1)
            AudioFormat.CHANNEL_OUT_MONO else AudioFormat.CHANNEL_OUT_STEREO

        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val format = AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(info.sampleRate)
            .setChannelMask(channelMask)
            .build()

        val track = AudioTrack.Builder()
            .setAudioAttributes(attrs)
            .setAudioFormat(format)
            .setTransferMode(AudioTrack.MODE_STATIC) // load once, play many
            .setBufferSizeInBytes(info.pcm.size)
            .build()

        // Load PCM into the track’s buffer
        val written = track.write(info.pcm, 0, info.pcm.size, AudioTrack.WRITE_BLOCKING)
        require(written == info.pcm.size) { "Failed to load audio" }

        val bytesPerFrame = info.channels * 2 // 16-bit
        val totalFrames = info.pcm.size / bytesPerFrame
        return AudioTrackClip(track, info.sampleRate, info.channels, info.lengthSec, totalFrames)
    }

    // reads wav length in seconds
    actual fun readInfo(bytes: ByteArray): Int? = parseWav(bytes)?.lengthSec

    // --- helpers ---
    private suspend fun fetch(url: String): ByteArray = withContext(Dispatchers.IO) {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.inputStream.use { it.readBytes() }
    }
}

/* -------------------- Implementation -------------------- */

private class AudioTrackClip(
    private val track: AudioTrack,
    private val sampleRate: Int,
    private val channels: Int,
    override val length: Int,
    private val totalFrames: Int
) : WaveClip {

    @Volatile
    private var closed = false

    override val isPlaying: Boolean
        get() = track.playState == AudioTrack.PLAYSTATE_PLAYING

    override val progress: Int
        get() = (minOf(track.playbackHeadPosition, totalFrames).toDouble() / sampleRate).toInt()

    override suspend fun play(onProgress: suspend (Int) -> Unit) {
        ensureOpen()
        track.play()

        while (!closed) {
            val head = track.playbackHeadPosition
            onProgress((minOf(head, totalFrames).toDouble() / sampleRate).toInt())
            if (head >= totalFrames || track.playState != AudioTrack.PLAYSTATE_PLAYING) break
            delay(200)
        }

        // Ensure we rewind for the next voyage
        runCatching { track.stop() }

        onProgress(length)
    }

    override fun pause() {
        if (!closed) track.pause()
    }

    override fun reset() {
        if (closed) return
        runCatching { track.stop() } // STATIC: stop() rewinds head to 0
    }

    override fun close() {
        if (closed) return
        closed = true
        runCatching { track.stop() }
        track.release()
    }

    private fun ensureOpen() { check(!closed) { "Clip closed" } }
}

/* -------------------- WAV parsing (PCM 16-bit) -------------------- */

private data class WavInfo(
    val sampleRate: Int,
    val channels: Int,
    val encoding: Int,
    val pcm: ByteArray,      // little-endian PCM 16-bit
    val lengthSec: Int
)

private fun parseWav(bytes: ByteArray): WavInfo? {
    if (bytes.size < 44) return null
    fun str(off: Int, n: Int) = bytes.copyOfRange(off, off + n).toString(Charsets.US_ASCII)
    if (str(0, 4) != "RIFF" || str(8, 4) != "WAVE") return null

    var offset = 12
    var sampleRate: Int? = null
    var channels: Int? = null
    var bitsPerSample: Int? = null
    var byteRate: Int? = null
    var dataPos: Int? = null
    var dataSize: Int? = null

    while (offset + 8 <= bytes.size && (dataPos == null || sampleRate == null)) {
        val id = str(offset, 4)
        val size = ByteBuffer.wrap(bytes, offset + 4, 4).order(ByteOrder.LITTLE_ENDIAN).int
        val body = offset + 8
        if (body + size > bytes.size) break

        when (id) {
            "fmt " -> {
                if (size >= 16) {
                    val bb = ByteBuffer.wrap(bytes, body, size).order(ByteOrder.LITTLE_ENDIAN)
                    val audioFormat = bb.short.toInt() and 0xFFFF
                    channels = bb.short.toInt() and 0xFFFF
                    sampleRate = bb.int
                    byteRate = bb.int
                    /* blockAlign */ bb.short
                    bitsPerSample = bb.short.toInt() and 0xFFFF
                    if (audioFormat != 1) return null // PCM only
                }
            }
            "data" -> {
                dataPos = body
                dataSize = size
            }
        }
        // chunks are word-aligned
        offset = body + ((size + 1) and -2)
    }

    val sr = sampleRate ?: return null
    val ch = channels ?: return null
    val bps = bitsPerSample ?: return null
    val br = byteRate ?: return null
    val pos = dataPos ?: return null
    val dsz = dataSize ?: return null
    if (bps != 16 || br <= 0) return null
    if (pos + dsz > bytes.size) return null

    val pcm = bytes.copyOfRange(pos, pos + dsz)
    val secs = (dsz.toDouble() / br.toDouble()).roundToInt().coerceAtLeast(0)

    return WavInfo(
        sampleRate = sr,
        channels = ch,
        encoding = AudioFormat.ENCODING_PCM_16BIT,
        pcm = pcm,
        lengthSec = secs
    )
}
