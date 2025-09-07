package pondui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Narrathon",
    ) {
        App()
    }
}