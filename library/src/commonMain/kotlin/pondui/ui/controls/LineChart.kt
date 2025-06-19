package pondui.ui.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import pondui.ui.theme.DefaultColors.swatches
import pondui.ui.theme.Pond
import pondui.utils.mix

@Composable
fun LineChart(
    arrays: List<ChartArray>,
    config: ChartConfig = ChartConfig(),
    glowColor: Color? = Pond.colors.glow,
    minSize: DpSize = DpSize(100.dp, 50.dp),
    parameters: CartesianParameters = remember { gatherParameters(arrays) },
    modifier: Modifier = Modifier
) {
    var animationTarget by remember { mutableStateOf(if (config.isAnimated) 0f else 1f) }
    LaunchedEffect(Unit) {
        animationTarget = 1f
    }
    val animation by animateFloatAsState(animationTarget, tween(1000))

    val colors = remember { arrays.mapIndexed { i, a -> a.color ?: swatches[i] } }

    Box(
        modifier = modifier.sizeIn(minWidth = minSize.width, minHeight = minSize.height)
            .drawWithCache {
                val chartMinX = if (config.leftAxis != null) CHART_MARGIN.dp.toPx() else 0f
                val chartMaxX = if (config.rightAxis != null) size.width - CHART_MARGIN.dp.toPx() else size.width
                val chartRangeX = chartMaxX - chartMinX

                val dataMinX = parameters.minX
                val dataMinY = parameters.minY
                val dataRangeX = parameters.rangeX.takeIf { it != 0f } ?: 1f
                val dataRangeY = parameters.rangeY.takeIf { it != 0f } ?: 1f

                // compute pixels per unit
                val scaleX = chartRangeX  / dataRangeX
                val scaleY = size.height / dataRangeY
                val paths = arrays.mapIndexed { i, array ->
                    val color = colors[i]
                    val path = Path()
                    var prevX: Float? = null
                    var prevY: Float? = null
                    array.values.forEachIndexed { idx, value ->
                        val x = (value.x - dataMinX) * scaleX + chartMinX
                        val y = size.height - (value.y - dataMinY) * scaleY
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
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round,
                        pathEffect = PathEffect.cornerPathEffect(16.dp.toPx())
                    )
                    val brush = glowColor?.let {
                        Brush.verticalGradient(
                            colors = listOf(color, mix(it, color)),
                            startY = size.height,
                            endY = 0f
                        )
                    } ?: SolidColor(color)
                    PathDefinition(path, stroke, brush)
                }
                onDrawBehind {
                    for ((path, stroke, brush) in paths) {
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
            },
    )
}

const val CHART_MARGIN = 30

@Stable
@Immutable
data class ChartConfig(
    val isAnimated: Boolean = true,
    val leftAxis: VerticalAxisConfig? = null,
    val rightAxis: VerticalAxisConfig? = null,
)

@Stable
@Immutable
data class VerticalAxisConfig(
    val values: List<Float>,
    val color: Color? = null
)

@Stable
@Immutable
data class ChartArray(
    val values: List<ChartValue>,
    val label: String? = null,
    val color: Color? = null,
    val isBezier: Boolean = true,
)

data class ChartValue(
    val x: Float,
    val y: Float,
    val key: Any
)

data class CartesianParameters(
    val maxX: Float,
    val minX: Float,
    val maxY: Float,
    val minY: Float,
) {
    val rangeX get () = maxX - minX
    val rangeY get () = maxY - minY
}

private data class PathDefinition(
    val path: Path,
    val stroke: Stroke,
    val brush: Brush
)

private fun gatherParameters(arrays: List<ChartArray>): CartesianParameters {
    var xMin = Float.MAX_VALUE
    var xMax = Float.MIN_VALUE
    var yMin = Float.MAX_VALUE
    var yMax = Float.MIN_VALUE
    for (array in arrays) {
        for (value in array.values) {
            if (value.x < xMin) xMin = value.x
            if (value.x > xMax) xMax = value.x
            if (value.y < yMin) yMin = value.y
            if (value.y > yMax) yMax = value.y
        }
    }
    return CartesianParameters(
        maxX = xMax,
        minX = xMin,
        maxY = yMax,
        minY = yMin,
    )
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