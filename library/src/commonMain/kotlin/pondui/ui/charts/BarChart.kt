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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun <T> BarChart(
    array: BarChartArray<T>,
    config: ChartConfig,
    modifier: Modifier = Modifier,
) {
    if (array.values.isEmpty()) return
    println(array.values)
    var animationTarget by remember { mutableStateOf(if (config.isAnimated) 0f else 1f) }
    LaunchedEffect(Unit) {
        animationTarget = 1f
    }
    val animation by animateFloatAsState(animationTarget, tween(2000))
    val textRuler = rememberTextMeasurer()
    var chartBarsCache by remember { mutableStateOf<List<ChartBar>?>(null) }

    var pointerTarget by remember { mutableStateOf<ChartBar?>(null) }
    var pointerTargetNow by remember { mutableStateOf<ChartBar?>(null) }
    var pointerTargetPrev by remember { mutableStateOf<ChartBar?>(null) }
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

                val chartScope = gatherBarChartScope(config, array, textRuler)

                val bars = chartScope.gatherChartBars(array)
                chartBarsCache = bars

                val leftAxisLabels = chartScope.gatherLeftAxisLabels()
                val rightAxisLabels = chartScope.gatherRightAxisLabels()
                val bottomAxisLabels = chartScope.gatherBottomAxisLabels()

                val horizontalRule = chartScope.gatherHorizontaRule()

                onDrawBehind {
                    horizontalRule?.let { drawHorizontalRule(it, chartScope, animation) }

                    leftAxisLabels?.let { drawAxisLabels(it, animation) }
                    rightAxisLabels?.let { drawAxisLabels(it, animation) }
                    bottomAxisLabels?.let { drawAxisLabels(it, animation) }

                    drawChartBars(
                        bars = bars,
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
//                                val chartLines = chartBarsCache ?: continue
//                                pointerTarget = detectPointerTarget(
//                                    event.changes.first().position,
//                                    chartLines,
//                                    pointerMinDistanceSquared
//                                )
                            }
                        }
                    }
                }
            }
    )
}
