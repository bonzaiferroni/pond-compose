package pondui.ui.services

import androidx.compose.runtime.Composable
import java.util.Locale

@Composable
actual fun rememberTextSpeaker(
    locale: Locale,
    onDone: (String) -> Unit
): (String) -> Unit {
    return { println("speaking: $it") }
}