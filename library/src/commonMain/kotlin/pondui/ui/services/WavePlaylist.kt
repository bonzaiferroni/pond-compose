package pondui.ui.services

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import pondui.ui.core.ModelState
import pondui.ui.core.SubModel

class WavePlaylist(
    override val viewModel: ViewModel,
    private val wavePlayer: WavePlayer = WavePlayer()
): SubModel<WavePlaylistState>() {
    override val state = ModelState(WavePlaylistState())

    private val clips = mutableListOf<WaveClip>()
    private var job: Job? = null

    fun <T> load(items: List<T>, resetClips: Boolean = true, provideBytes: (T) -> ByteArray) {
        if (resetClips) {
            job?.cancel()
            clips.forEach { it.close() }
            clips.clear()
        }
        items.forEachIndexed { index, item ->
            if (clips.size > index) return@forEachIndexed
            clips.add(wavePlayer.getClip(provideBytes(item)))
        }
        setState { state ->
            state.copy(
                lengthMillis = clips.sumOf { it.length },
                progress = stateNow.progress ?: 0,
                count = clips.size,
            )
        }
    }

    fun togglePlay() {
        if (clips.isEmpty()) return
        var index = state.value.index ?: 0
        var clip = clips[index]
        if (clip.isPlaying) {
            job?.cancel()
            clip.pause()
            setState { it.copy(isPlaying = false) }
        } else {
            playAtIndex(index)
        }
    }

    fun playFrom(index: Int) {
        cancelCurrentPlayback()
        playAtIndex(index)
    }

    private fun cancelCurrentPlayback() {
        if (!stateNow.isPlaying) return
        val index = stateNow.index ?: return
        job?.cancel()
        val clip = clips[index]
        clip.pause()
        clip.reset()
    }

    private fun playAtIndex(index: Int) {
        if (index >= clips.size || index < 0) return
        var index = index
        job?.cancel()
        job = ioLaunch {
            while (index < clips.size) {
                val clip = clips[index]
                val previousProgress = clips.take(index).sumOf { it.length }
                clip.play { progress ->
                    setStateWithMain { it.copy(
                        index = index,
                        progress = previousProgress + progress,
                        isPlaying = true,
                        clipProgress = progress
                    )}
                }
                clip.reset()
                index++
            }
            setStateWithMain { it.copy(isPlaying = false)}
        }
    }
}

data class WavePlaylistState(
    val lengthMillis: Int = 0,
    val progress: Int? = null,
    val clipProgress: Int? = null,
    val count: Int = 0,
    val index: Int? = null,
    val isPlaying: Boolean = false,
)