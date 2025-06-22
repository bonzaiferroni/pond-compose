package pondui.ui.charts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp

internal data class ChartHorizontalRule(
    val stamp: PathEffect,
    val lines: List<ChartAxisLine>
)

internal fun ChartScope.gatherHorizontaRule(): ChartHorizontalRule? {
    val leftAxis = this.leftAxis ?: return null
    val startColor = leftAxis.color
    val endColor = rightAxis?.color ?: leftAxis.color
    val scaleY = leftAxis.dimension.scalePx
    val minY = leftAxis.dimension.min

    return ChartHorizontalRule(
        stamp = PathEffect.stampedPathEffect(
            shape = Path().apply {
                val dotSize = 4.dp.toPx()
                addOval(Rect(0f, 0f, dotSize, dotSize))
            },
            advance = 8.dp.toPx(),
            phase = 0f,
            style = StampedPathEffectStyle.Translate
        ),
        lines = leftAxis.values.map { axisValue ->
            val valuePx = (axisValue.value - minY).toFloat() * scaleY
            val height = sizePx.height - chartMinY - valuePx - horizontalLineWidthPx / 2
            ChartAxisLine(
                start = Offset(x = chartMinX, y = height),
                end = Offset(x = chartMaxX, y = height),
                brush = Brush.horizontalGradient(
                    colors = listOf(startColor, endColor),
                    startX = chartMinX,
                    endX = chartMaxX
                )
            )
        }
    )
}

internal fun DrawScope.drawHorizontalRule(
    rule: ChartHorizontalRule,
    chartScope: ChartScope,
    animation: Float
) {
    rule.lines.forEachIndexed { i, line ->
        val alpha = -i + animation * rule.lines.size
        drawLine(
            brush = line.brush,
            start = line.start,
            end = line.end,
            strokeWidth = chartScope.horizontalLineWidthPx,
            alpha = .5f * alpha.coerceIn(0f, 1f),
            pathEffect = rule.stamp
        )
    }
}