package pondui.utils

import androidx.compose.ui.graphics.Color

fun Color.darken(amount: Float = 0.2f): Color {
    val r = (red * 255 - amount * 255).coerceIn(0f, 255f)
    val g = (green * 255 - amount * 255).coerceIn(0f, 255f)
    val b = (blue * 255 - amount * 255).coerceIn(0f, 255f)

    return Color(r / 255f, g / 255f, b / 255f, alpha)
}

fun Color.lighten(amount: Float = 0.2f) = darken(-amount)

fun mix(c1: Color, c2: Color): Color =
    Color(
        red   = (c1.red   + c2.red)   / 2f,
        green = (c1.green + c2.green) / 2f,
        blue  = (c1.blue  + c2.blue)  / 2f,
        alpha = (c1.alpha + c2.alpha) / 2f
    )

