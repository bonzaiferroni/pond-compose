package pondui.ui.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.SessionToken
import android.view.KeyEvent
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.source.SilenceMediaSource
import androidx.media3.session.MediaButtonReceiver
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

@Composable
actual fun MediaEventEffect(onEvent: (MediaEvent) -> Unit) {
    val context: Context = LocalContext.current
    DisposableEffect(onEvent) {
        println("setting up!")
        MediaEventReceiver.register(onEvent)
        // Start the foreground service.  Starting as a foreground service is
        // required so that the system does not stop it when the app is in
        // background; this ensures events continue when the screen is off.
        val intent = Intent(context, PlaybackService::class.java)
        context.startForegroundService(intent)
        onDispose {
            // Unregister callback and stop the service
            MediaEventReceiver.unregister()
            context.stopService(intent)
        }
    }
}

/**
 * Foreground service that hosts the ExoPlayer and MediaSession.  It listens for
 * hardware media key events and forwards them to MediaEventReceiver.
 */
class PlaybackService : MediaSessionService() {
    private lateinit var player: ExoPlayer
    private lateinit var session: MediaSession


    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        val audioAttrs = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        player = ExoPlayer.Builder(this).build().also {
            it.setAudioAttributes(audioAttrs, /* handleAudioFocus= */ true)
        }

        session = MediaSession.Builder(this, player)
            .setCallback(object : MediaSession.Callback {
                override fun onMediaButtonEvent(
                    session: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    intent: Intent
                ): Boolean {
                    val ev: KeyEvent? = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)
                    if (ev?.action == KeyEvent.ACTION_DOWN) {
                        when (ev.keyCode) {
                            KeyEvent.KEYCODE_MEDIA_PLAY,
                            KeyEvent.KEYCODE_MEDIA_PAUSE,
                            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> MediaEventReceiver.sendEvent(MediaEvent.PlayPause)
                            KeyEvent.KEYCODE_MEDIA_NEXT -> MediaEventReceiver.sendEvent(MediaEvent.Next)
                            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> MediaEventReceiver.sendEvent(MediaEvent.Previous)
                        }
                    }
                    return true
                }
            })
            .build()

        // Keep the session “active” with silent media
        val silence = SilenceMediaSource(/* durationUs = */ 60L * C.MICROS_PER_SECOND)
        player.setMediaSource(silence)
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.playWhenReady = true
        player.prepare()

        startForeground(NOTIFICATION_ID, buildNotification())
    }

    private fun buildNotification(): Notification {
        val channelId = "media_event_channel"
        val nm = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            channelId,
            "Media events",
            NotificationManager.IMPORTANCE_LOW
        )
        nm.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Media Event Listener")
            .setContentText("Listening for media buttons")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            // Launch the main activity when tapping the notification (optional)
            .setContentIntent(PendingIntent.getActivity(
                this,
                0,
                packageManager.getLaunchIntentForPackage(packageName),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            ))
        return builder.build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession = session

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        session.release()
        player.release()
        super.onDestroy()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}

// Helper object used to relay events from the service to the app layer.
object MediaEventReceiver {
    @Volatile
    private var callback: ((MediaEvent) -> Unit)? = null

    /**
     * Called by the service when a hardware key is received.  This method
     * dispatches the event to the currently registered callback on the main thread.
     */
    fun sendEvent(event: MediaEvent) {
        callback?.invoke(event)
    }

    fun register(callback: (MediaEvent) -> Unit) {
        this.callback = callback
    }

    fun unregister() {
        this.callback = null
    }
}

