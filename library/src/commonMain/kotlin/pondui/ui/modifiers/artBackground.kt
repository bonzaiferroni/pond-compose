package pondui.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import pondui.ui.theme.Pond
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun Modifier.artBackground(
    intensity: Float = .1f,
    colors: List<Color> = listOf(Pond.colors.swatches[0], Pond.colors.swatches[1], Pond.colors.swatches[2]),
    circles: Int = 10
): Modifier {
    val rng = remember { Random(Random.nextInt()) }
    return drawBehind {
        val w = size.width
        val h = size.height
        if (w <= 0f || h <= 0f) return@drawBehind

        val minDim = min(w, h)
        val minR = 0.1f * minDim
        val maxR = 0.4f * minDim
        val minAlpha = intensity * .05f
        val maxAlpha = intensity * .3f

        // grid for even coverage
        val cols = ceil(sqrt(circles.toFloat())).toInt().coerceAtLeast(1)
        val rows = ((circles + cols - 1) / cols).coerceAtLeast(1)
        val cellW = w / cols
        val cellH = h / rows

        repeat(circles) { i ->
            val col = i % cols
            val row = i / cols

            // jittered center within the cell
            var x = (col + rng.nextFloat()) * cellW
            var y = (row + rng.nextFloat()) * cellH

            // shape max radius by horizontal distance from center
            val dx = abs(x - w * 0.5f) / (w * 0.5f) // 0 at center, 1 at edges
            val shaped = (1f - dx) * (1f - dx)                 // ease-in toward center
            val localMaxR = minR.coerceAtLeast(minR + (maxR - minR) * shaped)

            val r = rng.nextFloat() * (localMaxR - minR) + minR

            // clamp after radius chosen
            x = x.coerceIn(r, w - r)
            y = y.coerceIn(r, h - r)

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