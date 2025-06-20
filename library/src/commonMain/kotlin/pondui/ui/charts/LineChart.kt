package pondui.ui.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LineChart(
    arrays: List<ChartArray>,
    config: ChartConfig,
    dataScope: DataScope = remember { gatherDataScope(arrays) },
    modifier: Modifier = Modifier
) {
    var animationTarget by remember { mutableStateOf(if (config.isAnimated) 0f else 1f) }
    LaunchedEffect(Unit) {
        animationTarget = 1f
    }
    val animation by animateFloatAsState(animationTarget, tween(2000))
    val textRuler = rememberTextMeasurer()
    var chartScopeCache by remember { mutableStateOf<ChartScope?>(null) }
    var chartLinesCache by remember { mutableStateOf<List<ChartLine>?>(null)}

    var pointerTarget by remember { mutableStateOf<ChartPoint?>(null) }
    var pointerTargetNow by remember { mutableStateOf<ChartPoint?>(null) }
    var pointerTargetPrev by remember { mutableStateOf<ChartPoint?>(null) }
    val pointerAnimatable = remember { Animatable(0f) }
    val focusAnimation by animateFloatAsState(if (pointerTarget != null) 1f else 0f)
    LaunchedEffect(pointerTarget) {
        pointerAnimatable.snapTo(0f)
        pointerTargetPrev = pointerTargetNow
        pointerTargetNow = pointerTarget
        pointerAnimatable.animateTo(1f)
    }

    val density = LocalDensity.current
    val pointerMinDistanceSquared = remember { with (density) { CHART_POINTER_TARGET_DISTANCE.dp.toPx().let { it * it } } }

    Box(
        modifier = modifier
            .drawWithCache {
                val chartScope = gatherChartScope(config, dataScope, textRuler)
                chartScopeCache = chartScope

                val lines = chartScope.gatherChartLines(arrays, config.glowColor)
                chartLinesCache = lines

                val leftAxisLabels = config.leftAxis?.let { chartScope.gatherLeftAxisLabels(it) }
                val rightAxisLabels = config.rightAxis?.let { chartScope.gatherRightAxisLabels(it) }
                val bottomAxisLabels = config.bottomAxis?.let { chartScope.gatherBottomAxisLabels(it) }

                val horizontalRule = config.leftAxis?.let { chartScope.gatherHorizontaRule(it, config.rightAxis) }

                onDrawBehind {
                    horizontalRule?.let { drawHorizontalRule(it, chartScope, animation) }

                    leftAxisLabels?.let { drawAxisLabels(it, animation) }
                    rightAxisLabels?.let { drawAxisLabels(it, animation) }
                    bottomAxisLabels?.let { drawAxisLabels(it, animation) }
                }
            }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .drawBehind {
                    val chartScope = chartScopeCache
                    val lines = chartLinesCache
                    if (chartScope == null || lines == null) return@drawBehind

                    drawChartLines(lines, animation, focusAnimation)
                    drawChartPoints(
                        lines = lines,
                        chartScope = chartScope,
                        chartConfig = config,
                        animation = animation,
                        pointerTarget = pointerTargetNow,
                        pointerTargetPrev = pointerTargetPrev,
                        pointerAnimation = pointerAnimatable.value
                    )
                }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Exit -> {
                                    pointerTarget = null
                                }
                                PointerEventType.Move -> {
                                    val chartLines = chartLinesCache ?: continue
                                    pointerTarget = detectPointerTarget(
                                        event.changes.first().position,
                                        chartLines,
                                        pointerMinDistanceSquared
                                    )
                                }
                            }
                        }
                    }

                }
        )
    }
}

internal fun detectPointerTarget(
    pointerPos: Offset,
    chartLines: List<ChartLine>,
    minDistanceSquared: Float
): ChartPoint? {
    var closestPoint: ChartPoint? = null
    var closestDistance = Float.MAX_VALUE
    for (line in chartLines) {
        for (point in line.points) {
            val distanceSquared = distanceSquared(pointerPos, point.offset)
            if (distanceSquared > minDistanceSquared || distanceSquared > closestDistance) continue
            closestDistance = distanceSquared
            closestPoint = point
        }
    }
    return closestPoint
}


fun distanceSquared(a: Offset, b: Offset): Float {
    val dx = a.x - b.x
    val dy = a.y - b.y
    return dx * dx + dy * dy
}

const val CHART_POINTER_TARGET_DISTANCE = 30

internal data class ChartAxisLine(
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
    val contentColor: Color,
    val isAnimated: Boolean = true,
    val glowColor: Color? = null,
    val leftAxis: AxisConfig? = null,
    val rightAxis: AxisConfig? = null,
    val bottomAxis: AxisConfig? = null,
)

@Stable
@Immutable
data class AxisConfig(
    val values: List<AxisValue>,
    val color: Color
)

data class AxisValue(
    val value: Float,
    val label: String = value.toInt().toString()
)

@Stable
@Immutable
data class ChartArray(
    val values: List<ChartValue>,
    val color: Color,
    val label: String? = null,
    val isBezier: Boolean = true,
)

data class ChartValue(
    val x: Float,
    val y: Float,
    val key: Any
)

data class DataScope(
    val maxX: Float,
    val minX: Float,
    val maxY: Float,
    val minY: Float,
) {
    val rangeX get() = maxX - minX
    val rangeY get() = maxY - minY
}

internal fun gatherDataScope(arrays: List<ChartArray>): DataScope {
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
    return DataScope(
        maxX = xMax,
        minX = xMin,
        maxY = yMax,
        minY = yMin,
    )
}