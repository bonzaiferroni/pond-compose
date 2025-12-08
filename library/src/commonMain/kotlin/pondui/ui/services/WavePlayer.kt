package pondui.ui.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import java.io.Closeable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WavePlayer() {
    suspend fun play(url: String)
    suspend fun play(bytes: ByteArray)
    suspend fun play(pcm: ShortArray, sampleRate: Int = 44_100)
    fun getClip(bytes: ByteArray): WaveClip
    fun readInfo(bytes: ByteArray): Int?
    fun getStream(sampleRate: Int = 44_100): WaveStream
}

interface WaveClip: Closeable {
    suspend fun play(onProgress: suspend (Int) -> Unit = { })
    fun pause()
    fun reset()
    val length: Int
    val progress: Int
    val isPlaying: Boolean
}

interface WaveStream: Closeable {
    fun write(chunk: ByteArray, length: Int = chunk.size)
}

@Composable
fun ProvideWavePlayer(
    content: @Composable () -> Unit
) {
    val player = remember { WavePlayer() }
    CompositionLocalProvider(LocalWavePlayer provides player) {
        content()
    }
}

@Composable
fun PlayWave(url: String) {
    val player = LocalWavePlayer.current

    LaunchedEffect(url) {
        player.play(url)
    }
}

val LocalWavePlayer = staticCompositionLocalOf<WavePlayer> { error("WavePlayer not initialized") }

