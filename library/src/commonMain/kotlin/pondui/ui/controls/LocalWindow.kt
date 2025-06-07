package pondui.ui.controls

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

val LocalAppWindow = compositionLocalOf<AppWindow> {
    error("No window provided")
}

data class AppWindow(
    val width: Int,
    val height: Int,
    val density: Density,
) {
    val widthSizeClass = with(density) { width.toDp().let {
        when {
            it < 480.dp -> WindowSizeClass.Compact
            it < 840.dp -> WindowSizeClass.Medium
            else -> WindowSizeClass.Expanded
        }
    } }
}

enum class WindowSizeClass {
    Compact,
    Medium,
    Expanded
}