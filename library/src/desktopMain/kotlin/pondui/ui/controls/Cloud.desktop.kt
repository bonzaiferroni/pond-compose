package pondui.ui.controls

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
actual fun provideCloudDialogProperties() = DialogProperties(
    scrimColor = Color.Transparent,
    usePlatformDefaultWidth = false
)