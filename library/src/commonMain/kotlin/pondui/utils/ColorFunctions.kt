package pondui.utils

import androidx.compose.ui.graphics.Color

fun Color.darken(amount: Float = 0.2f): Color {
    val r = (red * 255 - amount * 255).coerceIn(0f, 255f)
    val g = (green * 255 - amount * 255).coerceIn(0f, 255f)
    val b = (blue * 255 - amount * 255).coerceIn(0f, 255f)

    return Color(r / 255f, g / 255f, b / 255f, alpha)
}

fun Color.lighten(amount: Float = 0.2f) = darken(-amount)

fun mix(c1: Color, c2: Color, ratio: Float = 0.5f) = Color(
    red   = c1.red   * (1 - ratio) + c2.red   * ratio,
    green = c1.green * (1 - ratio) + c2.green * ratio,
    blue  = c1.blue  * (1 - ratio) + c2.blue  * ratio,
    alpha = c1.alpha * (1 - ratio) + c2.alpha * ratio
)

