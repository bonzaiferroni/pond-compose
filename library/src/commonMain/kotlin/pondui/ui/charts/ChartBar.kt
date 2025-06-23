package pondui.ui.charts

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import pondui.utils.mix

data class ChartBar(
    val centerPx: Float,
    val heightPx: Float,
    val color: Color,
    val brush: Brush,
    val labelX: String,
    val labelY: String,
    val side: AxisSide,
)

fun <T> BarChartScope.gatherChartBars(array: BarChartArray<T>): List<ChartBar> {
    val chartBars = mutableListOf<ChartBar>()
    val (scalePxX, _, minX) = dimensionX
    val (scalePxY, _, minY) = dimensionY
    val brushes = mutableMapOf<Color, Brush>()
    array.values.sortedBy { array.provideX(it) }. forEachIndexed { i, value ->
        val valueX = array.provideX(value)
        val valueY = array.provideY(value)
        val x = chartLeftMarginPx + barWidthPx / 2 + valueOffsetPx(valueX, minX, scalePxX)
        val y = valueOffsetPx(valueY, minY, scalePxY)

        val color = array.provideColor(value)
        if (!brushes.contains(color)) {
            val mixedColor = config.glowColor?.let { mix(it, color) } ?: color
            brushes[color] = Brush.verticalGradient(
                colors = listOf(color, mixedColor),
                startY = chartHeightPx,
                endY = 0f
            )
        }
        val brush = brushes.getValue(color)

        chartBars.add(ChartBar(
            centerPx = x,
            heightPx = y,
            color = array.provideColor(value),
            brush = brush,
            labelX = config.provideLabelX(valueX),
            labelY = array.provideLabelY(valueY),
            side = array.axis?.side ?: AxisSide.Left
        ))
    }
    return chartBars
}

internal fun DrawScope.drawChartBars(
    bars: List<ChartBar>,
    chartScope: BarChartScope,
    animation: Float,
    pointerTarget: ChartBar?,
    pointerTargetPrev: ChartBar?,
    pointerAnimation: Float,
) {
    bars.forEachIndexed { i, bar ->
        val indexAnimation = (-i + animation * bars.size).coerceIn(0f, 1f)
        // val bump = if (pointerTarget == point) pointerAnimation * chartScope.pointRadiusPx
        // else if (pointerTargetPrev == point) (1 - pointerAnimation) * chartScope.pointRadiusPx
        // else 0f

        // colored dot
        drawRoundRect(
            brush = bar.brush,
            topLeft = Offset(
                x = bar.centerPx - chartScope.barWidthPx / 2,
                y = chartScope.sizePx.height - chartScope.chartBottomMarginPx - bar.heightPx
            ),
            size = Size(
                width = chartScope.barWidthPx,
                height = bar.heightPx
            ),
            cornerRadius = CornerRadius(2f, 2f)
        )
    }
}