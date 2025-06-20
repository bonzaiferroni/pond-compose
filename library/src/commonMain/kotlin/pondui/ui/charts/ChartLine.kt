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
)

internal fun ChartScope.gatherChartLines(
    arrays: List<ChartArray>,
    glowColor: Color?,
): List<ChartLine> {
    val chartLines = mutableListOf<ChartLine>()
    arrays.forEachIndexed { i, array ->
        val color = array.color
        val mixedColor = glowColor?.let { mix(it, color) } ?: color
        val path = Path()
        var prevX: Float? = null
        var prevY: Float? = null
        val arrayPoints = mutableListOf<ChartPoint>()
        array.values.forEachIndexed { idx, value ->
            val x = chartMinX + (value.x - data.minX) * scaleX
            val y = sizePx.height - axisMarginPx - (value.y - data.minY) * scaleY

            arrayPoints.add(ChartPoint(Offset(x, y), mix(color, mixedColor, ((value.y - data.minY) / data.rangeY))))
            if (idx == 0) path.moveTo(x, y)
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
            startY = chartRangeY,
            endY = 0f
        )
        chartLines.add(ChartLine(ChartPath(path, stroke, brush), arrayPoints))
    }
    return chartLines
}

internal fun DrawScope.drawChartLines(lines: List<ChartLine>, animation: Float) {
    for (line in lines) {
        val (path, stroke, brush) = line.path
        if (animation == 1f) {
            drawPath(
                path = path,
                style = stroke,
                brush = brush,
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
            )
        }
    }
}

internal fun DrawScope.drawChartPoints(
    lines: List<ChartLine>,
    chartScope: ChartScope,
    chartConfig: ChartConfig,
    animation: Float
) {
    for (line in lines) {
        line.points.forEachIndexed { i, point ->
            val indexAnimation = (-i + animation * line.points.size).coerceIn(0f, 1f)
            val radius = chartScope.pointRadiusPx * indexAnimation
            // colored dot
            drawCircle(
                color  = point.color,
                radius = radius,
                center = point.offset,
                alpha = indexAnimation * .5f
            )
            // white “pupil”
            drawCircle(
                color  = chartConfig.contentColor,
                radius = radius / 2,
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