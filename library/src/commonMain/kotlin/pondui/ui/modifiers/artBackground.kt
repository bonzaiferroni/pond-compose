package pondui.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import pondui.ui.theme.Pond
import kotlin.math.min
import kotlin.random.Random

@Composable
fun Modifier.artBackground(
    intensity: Float = .1f,
    colors: List<Color> = listOf(Pond.colors.swatches[0], Pond.colors.swatches[1], Pond.colors.swatches[2]),
    circles: Int = 10
): Modifier {
    // Stable seed per size so it doesn’t flicker on redraw
    val seed = remember { Random.nextInt() }
    return drawBehind {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@drawBehind

        val rng = Random(seed)

        val minDim = min(w, h)
        val minR = 0.06f * minDim
        val maxR = 0.28f * minDim
        val minAlpha = intensity * .05f
        val maxAlpha = intensity * .3f

        repeat(circles) {
            val r = rng.nextFloat() * (maxR - minR) + minR
            val x = rng.nextFloat() * (w - 2f * r) + r
            val y = rng.nextFloat() * (h - 2f * r) + r
            val a = rng.nextFloat() * (maxAlpha - minAlpha) + minAlpha
            val color = colors[rng.nextInt(colors.size)]

            drawCircle(
                color = color.copy(alpha = a),
                radius = r,
                center = Offset(x, y)
            )
        }
    }
}