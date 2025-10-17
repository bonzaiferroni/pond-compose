package pondui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Color.darken(amount: Float = 0.2f): Color {
    val r = (red * 255 - amount * 255).coerceIn(0f, 255f)
    val g = (green * 255 - amount * 255).coerceIn(0f, 255f)
    val b = (blue * 255 - amount * 255).coerceIn(0f, 255f)

    return Color(r / 255f, g / 255f, b / 255f, alpha)
}

fun Color.lighten(amount: Float = 0.2f) = darken(-amount)

fun Color.electrify(
    saturationFactor: Float = 1.5f,
    brightnessFactor: Float = saturationFactor
) = run {
    // compute luma for hue-preserving saturation boost
    val luma = 0.3f * red + 0.59f * green + 0.11f * blue
    fun boost(c: Float) = (((c - luma) * saturationFactor) + luma) * brightnessFactor
    Color(
        red   = boost(red).coerceIn(0f, 1f),
        green = boost(green).coerceIn(0f, 1f),
        blue  = boost(blue).coerceIn(0f, 1f),
        alpha = alpha
    )
}

fun mix(c1: Color, c2: Color, ratio: Float = 0.5f) = Color(
    red   = c1.red   * (1 - ratio) + c2.red   * ratio,
    green = c1.green * (1 - ratio) + c2.green * ratio,
    blue  = c1.blue  * (1 - ratio) + c2.blue  * ratio,
    alpha = c1.alpha * (1 - ratio) + c2.alpha * ratio
)

fun Color.mixWith(color: Color, ratio: Float = 0.5f) = mix(this, color, ratio)

fun Color.glowWith(glow: Color, size: Size, ratio: Float = 0.4f) = Brush.linearGradient(
    colors = listOf(this.mixWith(glow, ratio), this),
    start = Offset(size.width * .5f, 0f),
    end = Offset(size.width * .6f, size.height)
)