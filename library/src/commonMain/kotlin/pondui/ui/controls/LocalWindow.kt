package pondui.ui.controls

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.compareTo

val LocalAppWindow = compositionLocalOf<AppWindow> {
    error("No window provided")
}

data class AppWindow(
    val width: Int,
    val height: Int,
    val scale: Float,
) {
    val widthSizeClass = (width * scale).let {
        when {
            it < 480f -> WindowSizeClass.Compact
            it < 840f -> WindowSizeClass.Medium
            else -> WindowSizeClass.Expanded
        }
    }
}

enum class WindowSizeClass {
    Compact,
    Medium,
    Expanded
}