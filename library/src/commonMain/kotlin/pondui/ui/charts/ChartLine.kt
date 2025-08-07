package pondui.ui.charts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import pondui.utils.mix

internal data class ChartLine(
    val path: ChartPath,
    val points: List<ChartPoint>
)

internal data class ChartPath(
    val path: Path,
    val stroke: Stroke,
    val brush: Brush
)

internal data class ChartPoint(
    val offset: Offset,
    val color: Color,
    val labelX: String,
    val labelY: String,
    val side: AxisSide
)

internal fun <T> LineChartScope.gatherChartLines(
    arrays: List<LineChartArray<T>>,
    provideX: (T) -> Double,
): List<ChartLine> {
    val chartLines = mutableListOf<ChartLine>()
    val (scalePxX, _, minX) = this.dimensionX
    arrays.forEachIndexed { i, array ->
        val dimensionY = dimensionsY[i]
        val (scalePxY, _, minY) = dimensionY
        val color = array.color
        val mixedColor = config.glowColor?.let { mix(it, color) } ?: color
        val path = Path()
        var prevX: Float? = null
        var prevY: Float? = null
        val arrayPoints = mutableListOf<ChartPoint>()
        array.values.sortedBy { provideX(it) }. forEachIndexed { i, value ->
            val valueX = provideX(value)
            val valueY = array.provideY(value)
            val x = chartLeftMarginPx + valueOffsetPx(valueX, minX, scalePxX)
            val y = sizePx.height - chartBottomMarginPx - valueOffsetPx(valueY, minY, scalePxY)

            arrayPoints.add(ChartPoint(
                offset = Offset(x, y),
                color = mix(color, mixedColor, ((valueY - minY).toFloat() / dimensionY.range)),
                labelX = config.provideLabelX(valueX),
                labelY = array.provideLabelY(valueY),
                side = array.axis?.side ?: AxisSide.Left
            ))
            if (i == 0) path.moveTo(x, y)
            else if (array.isBezier) path.drawBezier(
                prevX = prevX ?: x,
                prevY = prevY ?: y,
                currentX = x,
                currentY = y
            )
            else path.lineTo(x, y)
            prevX = x
            prevY = y
        }
        val stroke = Stroke(
            width = lineWidthPx,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        )
        val brush = Brush.verticalGradient(
            colors = listOf(color, mixedColor),
            startY = chartHeightPx,
            endY = 0f
        )
        chartLines.add(ChartLine(ChartPath(path, stroke, brush), arrayPoints))
    }
    return chartLines
}

internal fun DrawScope.drawChartLines(lines: List<ChartLine>, animation: Float, focusAnimation: Float) {
    val alpha = (1 - focusAnimation) * .5f + .5f
    for (line in lines) {
        val (path, stroke, brush) = line.path
        if (animation == 1f) {
            drawPath(
                path = path,
                style = stroke,
                brush = brush,
                alpha = alpha
            )
        } else {
            val pathMeasure = PathMeasure().apply {
                setPath(path, false)
            }
            val currentLength = animation * pathMeasure.length
            val partialPath = Path().apply {
                pathMeasure.getSegment(0f, currentLength, this, true)
            }
            drawPath(
                path = partialPath,
                brush = brush,
                style = stroke,
                alpha = alpha
            )
        }
    }
}

internal fun DrawScope.drawChartPoints(
    lines: List<ChartLine>,
    chartScope: LineChartScope,
    animation: Float,
    pointerTarget: ChartPoint?,
    pointerTargetPrev: ChartPoint?,
    pointerAnimation: Float
) {
    for (line in lines) {
        line.points.forEachIndexed { i, point ->
            val indexAnimation = (-i + animation * line.points.size).coerceIn(0f, 1f)
            val bump = if (pointerTarget == point) pointerAnimation * chartScope.pointRadiusPx
            else if (pointerTargetPrev == point) (1 - pointerAnimation) * chartScope.pointRadiusPx
            else 0f
            val radius = chartScope.pointRadiusPx * indexAnimation + bump
            // colored dot
            drawCircle(
                color = point.color,
                radius = radius / 2,
                center = point.offset,
                alpha = indexAnimation * .5f
            )
            // white “pupil”
            drawCircle(
                color = chartScope.config.contentColor,
                radius = radius / 4,
                center = point.offset,
                alpha = indexAnimation * .5f
            )
        }
    }
}

private fun Path.drawBezier(
    prevX: Float,
    prevY: Float,
    currentX: Float,
    currentY: Float
) {
    val controlPointDiv = 2.2f
    val controlX1 = prevX + (currentX - prevX) / controlPointDiv
    val controlX2 = currentX - (currentX - prevX) / controlPointDiv

    cubicTo(
        controlX1, prevY,
        controlX2, currentY,
        currentX, currentY
    )
}

internal fun valueOffsetPx(value: Double, minValue: Double, scalePx: Float) = (value - minValue).toFloat() * scalePx