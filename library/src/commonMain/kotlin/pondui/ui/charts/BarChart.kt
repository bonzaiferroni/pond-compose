package pondui.ui.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
    config: BarChartConfig<T>,
    modifier: Modifier = Modifier,
) {
    if (config.array.values.isEmpty()) return
    var displayedConfig by remember { mutableStateOf(config) }

    // chart drawing animator
    val animation = remember { Animatable(0f) }
    LaunchedEffect(config) {
        if (animation.value > 0f)
            animation.animateTo(0f, tween(800, easing = LinearEasing))
        displayedConfig = config
        animation.animateTo(1f, tween(1000, easing = LinearEasing))
    }

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

                val chartScope = gatherBarChartScope(displayedConfig, textRuler)
                val bars = chartScope.gatherChartBars(displayedConfig.array)
                chartBarsCache = bars

                val leftAxisLabels = chartScope.gatherLeftAxisLabels()
                val rightAxisLabels = chartScope.gatherRightAxisLabels()
                val bottomAxisLabels = chartScope.gatherBottomAxisLabels()

                val horizontalRule = chartScope.gatherHorizontaRule()

                onDrawBehind {
                    horizontalRule?.let { drawHorizontalRule(it, chartScope, animation.value) }

                    leftAxisLabels?.let { drawAxisLabels(it, animation.value) }
                    rightAxisLabels?.let { drawAxisLabels(it, animation.value) }
                    bottomAxisLabels?.let { drawAxisLabels(it, animation.value) }

                    drawChartBars(
                        bars = bars,
                        chartScope = chartScope,
                        animation = animation.value,
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
