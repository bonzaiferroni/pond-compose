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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pondui.ui.theme.DefaultColors.swatches
import pondui.ui.theme.Pond
import pondui.utils.mix
import kotlin.math.absoluteValue

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
    val animation by animateFloatAsState(animationTarget, tween(2000))

    val colors = remember { arrays.mapIndexed { i, a -> a.color ?: swatches[i] } }
    val textRuler = rememberTextMeasurer()
    val contentColor = config.contentColor ?: Pond.localColors.content

    Box(
        modifier = modifier.sizeIn(minWidth = minSize.width, minHeight = minSize.height)
            .drawWithCache {
                val labelHeight = CHART_AXIS_LABEL_HEIGHT.sp.toPx()
                val axisPadding = CHART_SIDE_AXIS_MARGIN.dp.toPx()
                // val bottomAxisMarginHeight = labelHeight + CHART_AXIS_PADDING.dp.toPx()
                val chartMinX = if (config.leftAxis != null) axisPadding else 0f
                val chartMaxX = if (config.rightAxis != null) size.width - axisPadding else size.width
                val chartMinY = labelHeight / 2
                val chartMaxY = if (config.bottomAxis != null) size.height - axisPadding else size.height
                val chartRangeX = chartMaxX - chartMinX
                val chartRangeY = chartMaxY - chartMinY

                val dataMinX = parameters.minX
                val dataMinY = parameters.minY
                val dataRangeX = parameters.rangeX.takeIf { it != 0f } ?: 1f
                val dataRangeY = parameters.rangeY.takeIf { it != 0f } ?: 1f

                // compute pixels per unit
                val scaleX = chartRangeX / dataRangeX
                val scaleY = chartRangeY / dataRangeY
                val points = mutableListOf<List<PointDefinition>>()
                val paths = arrays.mapIndexed { i, array ->
                    val color = colors[i]
                    val mixedColor = glowColor?.let { mix(it, color) } ?: color
                    val path = Path()
                    var prevX: Float? = null
                    var prevY: Float? = null
                    val arrayPoints = mutableListOf<PointDefinition>()
                    array.values.forEachIndexed { idx, value ->
                        val x = chartMinX + (value.x - dataMinX) * scaleX
                        val y = size.height - axisPadding - (value.y - dataMinY) * scaleY

                        arrayPoints.add(PointDefinition(Offset(x, y), mix(color, mixedColor, ((value.y - dataMinY) / dataRangeY))))
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
                    val brush = Brush.verticalGradient(
                        colors = listOf(color, mixedColor),
                        startY = chartRangeY,
                        endY = 0f
                    )
                    points.add(arrayPoints)
                    PathDefinition(path, stroke, brush)
                }
                val pointRadius = 8.dp.toPx()

                val labelPadding = CHART_AXIS_PADDING.dp.toPx()
                val labelFontSize = CHART_AXIS_LABEL_HEIGHT.sp

                val leftAxisLabels = config.leftAxis?.let { axisConfig ->
                    val color = axisConfig.color ?: swatches[0]
                    axisConfig.values.mapIndexed { i, axisValue ->
                        val result = textRuler.measure(
                            text = axisValue.label,
                            style = TextStyle(color = color, fontSize = labelFontSize)
                        )
                        LabelDefinition(
                            layoutResult = result,
                            topLeft = Offset(
                                x = 0f,
                                y = size.height - axisPadding - axisValue.value * scaleY - result.size.height / 2
                            )
                        )
                    }
                }
                val rightAxisLabels = config.rightAxis?.let { axisConfig ->
                    val color = axisConfig.color ?: swatches[1]
                    axisConfig.values.mapIndexed { i, axisValue ->
                        val result = textRuler.measure(
                            text = axisValue.label,
                            style = TextStyle(color = color, fontSize = labelFontSize)
                        )
                        LabelDefinition(
                            layoutResult = result,
                            topLeft = Offset(
                                x = size.width - result.size.width,
                                y = size.height - axisPadding - axisValue.value * scaleY - result.size.height / 2
                            )
                        )
                    }
                }
                val bottomAxisLabels = config.bottomAxis?.let { axisConfig ->
                    val color = axisConfig.color ?: contentColor
                    axisConfig.values.mapIndexed { i, axisValue ->
                        val result = textRuler.measure(
                            text = axisValue.label,
                            style = TextStyle(color = color, fontSize = labelFontSize)
                        )
                        LabelDefinition(
                            layoutResult = result,
                            topLeft = Offset(
                                x = chartMinX + axisValue.value * scaleX - result.size.width / 2f,
                                y = size.height - result.size.height
                            )
                        )
                    }
                }

                val horizontalLineWidth = 2.dp.toPx()
                val horizontalLines = config.leftAxis?.let { axisConfig ->
                    val startColor = axisConfig.color ?: swatches[0]
                    val endColor = config.rightAxis?.color ?: swatches[1] // startColor
                    axisConfig.values.map { axisValue ->
                        val height = size.height - axisPadding - axisValue.value * scaleY - horizontalLineWidth / 2
                        LineDefinition(
                            start = Offset(x = chartMinX, y = height),
                            end = Offset(x = chartMaxX, y = height),
                            brush = Brush.horizontalGradient(
                                colors = listOf(startColor, endColor),
                                startX = chartMinX,
                                endX   = chartMaxX
                            )
                        )
                    }
                }
                val horizontalLineStamp = PathEffect.stampedPathEffect(
                    shape   = Path().apply {
                        val dotSize = 4.dp.toPx()
                        addOval(Rect(0f, 0f, dotSize, dotSize))
                    },
                    advance = 8.dp.toPx(),
                    phase   = 0f,
                    style   = StampedPathEffectStyle.Translate
                )

                onDrawBehind {
                    horizontalLines?.forEachIndexed { i, line ->
                        val alpha = -i + animation * horizontalLines.size
                        drawLine(
                            brush = line.brush,
                            start = line.start,
                            end = line.end,
                            strokeWidth = horizontalLineWidth,
                            alpha = .5f * alpha.coerceIn(0f, 1f),
                            pathEffect = horizontalLineStamp
                        )
                    }

                    leftAxisLabels?.forEachIndexed { i, label ->
                        val alpha = -i + animation * leftAxisLabels.size
                        drawText(
                            textLayoutResult = label.layoutResult,
                            topLeft = label.topLeft,
                            alpha = alpha
                        )
                    }

                    rightAxisLabels?.forEachIndexed { i, label ->
                        val alpha = -i + animation * rightAxisLabels.size
                        drawText(
                            textLayoutResult = label.layoutResult,
                            topLeft = label.topLeft,
                            alpha = alpha
                        )
                    }

                    bottomAxisLabels?.forEachIndexed { i, label ->
                        val alpha = -i + animation * bottomAxisLabels.size
                        drawText(
                            textLayoutResult = label.layoutResult,
                            topLeft = label.topLeft,
                            alpha = alpha
                        )
                    }

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

                    for (arrayPoints in points) {
                        arrayPoints.forEachIndexed { i, point ->
                            val indexAnimation = (-i + animation * arrayPoints.size).coerceIn(0f, 1f)
                            val radius = pointRadius * indexAnimation
                            // colored dot
                            drawCircle(
                                color  = point.color,
                                radius = radius,
                                center = point.offset,
                                alpha = indexAnimation
                            )
                            // white “pupil”
                            drawCircle(
                                color  = contentColor,
                                radius = radius / 2,
                                center = point.offset,
                                alpha = indexAnimation
                            )
                        }
                    }
                }
            },
    )
}

internal data class PathDefinition(
    val path: Path,
    val stroke: Stroke,
    val brush: Brush
)

internal data class PointDefinition(
    val offset: Offset,
    val color: Color,
)

internal data class LabelDefinition(
    val layoutResult: TextLayoutResult,
    val topLeft: Offset
)

internal data class LineDefinition(
    val start: Offset,
    val end: Offset,
    val brush: Brush,
)

const val CHART_SIDE_AXIS_MARGIN = 30
const val CHART_AXIS_LABEL_HEIGHT = 12
const val CHART_AXIS_PADDING = CHART_AXIS_LABEL_HEIGHT / 2

@Stable
@Immutable
data class ChartConfig(
    val isAnimated: Boolean = true,
    val contentColor: Color? = null,
    val leftAxis: AxisConfig? = null,
    val rightAxis: AxisConfig? = null,
    val bottomAxis: AxisConfig? = null,
)

@Stable
@Immutable
data class AxisConfig(
    val values: List<AxisValue>,
    val color: Color? = null
)

data class AxisValue(
    val value: Float,
    val label: String = value.toInt().toString()
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
    val rangeX get() = maxX - minX
    val rangeY get() = maxY - minY
}

internal fun gatherParameters(arrays: List<ChartArray>): CartesianParameters {
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