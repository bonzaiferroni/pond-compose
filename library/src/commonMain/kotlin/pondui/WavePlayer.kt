package pondui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.Job

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WavePlayer() {
    fun playNow(url: String)
    suspend fun play(url: String)
    suspend fun play(bytes: ByteArray, onProgress: ((Int) -> Unit)?)
    fun pause()

    fun readInfo(bytes: ByteArray): Int?
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
        player.playNow(url)
    }
}

val LocalWavePlayer = staticCompositionLocalOf<WavePlayer> { error("WavePlayer not initialized") }
