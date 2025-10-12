package pondui.ui.services

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.concurrent.thread
import kotlin.math.max
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import pondui.ui.controls.Text

@RequiresPermission(Manifest.permission.RECORD_AUDIO)
actual fun createMicStream(spec: AudioSpec, onChunk: (Pcm16, Int) -> Unit): MicStream {
    require(spec.format == PcmFormat.S16LE) { "Only S16LE supported" }
    val chMask = if (spec.channels == 1) AudioFormat.CHANNEL_IN_MONO else AudioFormat.CHANNEL_IN_STEREO
    val minBuf = AudioRecord.getMinBufferSize(
        spec.sampleRate,
        chMask,
        AudioFormat.ENCODING_PCM_16BIT
    )
    val bytesPerFrame = 2 * spec.channels
    val targetBytes = spec.framesPerChunk * bytesPerFrame
    val bufferBytes = max(minBuf, targetBytes * 2)

    val recorder = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        spec.sampleRate,
        chMask,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferBytes
    )

    return object : MicStream {
        @Volatile private var running = false
        private var t: Thread? = null

        override fun start() {
            if (running) return
            running = true
            recorder.startRecording()
            t = thread(isDaemon = true, name = "MicStream-Android") {
                val chunk = ShortArray(spec.framesPerChunk * spec.channels)
                while (running) {
                    var filled = 0
                    while (running && filled < chunk.size) {
                        val n = recorder.read(chunk, filled, chunk.size - filled)
                        if (n > 0) filled += n else break
                    }
                    if (filled > 0) {
                        val frames = filled / spec.channels
                        onChunk(chunk, frames)
                    }
                }
            }
        }

        override fun stop() {
            running = false
            t?.join()
            recorder.stop()
            recorder.release()
        }
    }
}

private const val REQ_RECORD_AUDIO = 42

fun ensureMicPermission(activity: Activity, onGranted: () -> Unit) {
    if (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // Already have it
        onGranted()
    } else {
        // Ask politely
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            REQ_RECORD_AUDIO
        )
    }
}

@Composable
actual fun MicPermissionRequester(
    deniedContent: @Composable () -> Unit,
    grantedContent: @Composable () -> Unit
) {
    var hasPermission by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            hasPermission
        } else {
            launcher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    val isOpen = hasPermission ?: return
    if (isOpen) {
        grantedContent()
    } else {
        deniedContent()
    }
}