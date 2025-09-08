package pondui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import coil3.request.Disposable
import kotlinx.coroutines.flow.StateFlow
import java.io.Closeable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WavePlayer() {
    suspend fun play(url: String)
    suspend fun play(bytes: ByteArray)
    fun getClip(bytes: ByteArray): WaveClip
    fun readInfo(bytes: ByteArray): Int?
}

interface WaveClip: Closeable {
    suspend fun play(onProgress: (Int) -> Unit)
    fun pause()
    val length: Int
    val progress: Int
    val isPlaying: Boolean
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

