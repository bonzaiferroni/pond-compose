package pondui.ui.services

import androidx.compose.runtime.Composable
import java.util.Locale

@Composable
expect fun rememberTextSpeaker(
    locale: Locale = Locale.getDefault(),
    onDone: (String) -> Unit = {}
): (String) -> Unit