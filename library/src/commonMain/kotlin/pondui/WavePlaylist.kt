package pondui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import pondui.ui.core.ModelState
import pondui.ui.core.SubModel

class WavePlaylist(
    override val viewModel: ViewModel,
    private val wavePlayer: WavePlayer = WavePlayer()
): SubModel<WavePlaylistState>() {
    override val state = ModelState(WavePlaylistState())

    private val clips = mutableListOf<WaveClip>()
    private var job: Job? = null

    fun <T> load(items: List<T>, provideBytes: (T) -> ByteArray) {
        job?.cancel()
        clips.forEach { it.close() }
        clips.clear()
        items.forEach { clips.add(wavePlayer.getClip(provideBytes(it))) }
        setState { state ->
            state.copy(
                lengthMillis = clips.sumOf { it.length },
                progress = 0,
                count = clips.size,
                index = 0,
                isPlaying = false,
            )
        }
    }

    fun togglePlay() {
        var index = state.value.index ?: return
        var clip = clips[index]
        if (clip.isPlaying) {
            clip.pause()
            job?.cancel()
            setState { it.copy(isPlaying = false) }
            return
        }

        job?.cancel()
        job = ioLaunch {
            while (index < clips.size) {
                clip = clips[index]
                val previousProgress = clips.take(index).sumOf { it.length }
                clip.play { progress ->
                    setState { it.copy(index = index, progress = previousProgress + progress, isPlaying = true)}
                }
                index++
            }
        }
    }
}

data class WavePlaylistState(
    val lengthMillis: Int = 0,
    val progress: Int? = null,
    val count: Int = 0,
    val index: Int? = null,
    val isPlaying: Boolean = false,
)