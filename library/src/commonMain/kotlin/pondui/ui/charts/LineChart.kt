package pondui.ui.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun <T> LineChart(
    config: LineChartConfig<T>,
    modifier: Modifier = Modifier,
) {
    var animationTarget by remember { mutableStateOf(if (config.isAnimated) 0f else 1f) }
    LaunchedEffect(Unit) {
        animationTarget = 1f
    }
    val animation by animateFloatAsState(animationTarget, tween(2000))
    val textRuler = rememberTextMeasurer()
    var chartLinesCache by remember { mutableStateOf<List<ChartLine>?>(null) }

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
    val pointerMinDistanceSquared =
        remember { with(density) { CHART_POINTER_TARGET_DISTANCE.dp.toPx().let { it * it } } }

    Box(
        modifier = modifier
            .drawWithCache {

                val chartScope = gatherChartScope(config, textRuler)

                val lines = chartScope.gatherChartLines(config.arrays, config.provideX)
                chartLinesCache = lines

                val leftAxisLabels = chartScope.gatherLeftAxisLabels()
                val rightAxisLabels = chartScope.gatherRightAxisLabels()
                val bottomAxisLabels = chartScope.gatherBottomAxisLabels()

                val horizontalRule = chartScope.gatherHorizontaRule()

                onDrawBehind {
                    horizontalRule?.let { drawHorizontalRule(it, chartScope, animation) }

                    leftAxisLabels?.let { drawAxisLabels(it, animation) }
                    rightAxisLabels?.let { drawAxisLabels(it, animation) }
                    bottomAxisLabels?.let { drawAxisLabels(it, animation) }

                    drawChartLines(lines, animation, focusAnimation)
                    drawChartPoints(
                        lines = lines,
                        chartScope = chartScope,
                        animation = animation,
                        pointerTarget = pointerTargetNow,
                        pointerTargetPrev = pointerTargetPrev,
                        pointerAnimation = pointerAnimatable.value
                    )
                }
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
