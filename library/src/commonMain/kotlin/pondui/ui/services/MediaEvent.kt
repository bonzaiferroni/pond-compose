package pondui.ui.services

import androidx.compose.runtime.Composable

enum class MediaEvent {
    PlayPause, Next, Previous
}

/**
 * Call inside your app’s root composition. Invokes [onEvent] for each media key.
 * Android: works with screen off when the MediaSession is active.
 * Desktop: works while the app window has focus (global hooks omitted for simplicity).
 */
@Composable
expect fun MediaEventEffect(onEvent: (MediaEvent) -> Unit)