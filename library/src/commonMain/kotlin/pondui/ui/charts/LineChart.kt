package pondui.ui.charts

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(
    arrays: List<ChartArray>,
    config: ChartConfig = ChartConfig(),
    minSize: DpSize = DpSize(100.dp, 50.dp),
    dataScope: DataScope = remember { gatherDataScope(arrays) },
    modifier: Modifier = Modifier
) {
    var animationTarget by remember { mutableStateOf(if (config.isAnimated) 0f else 1f) }
    LaunchedEffect(Unit) {
        animationTarget = 1f
    }
    val animation by animateFloatAsState(animationTarget, tween(2000))
    val textRuler = rememberTextMeasurer()

    Box(
        modifier = modifier.sizeIn(minWidth = minSize.width, minHeight = minSize.height)
            .drawWithCache {
                val chartScope = gatherChartScope(config, dataScope, textRuler)

                val lines = chartScope.gatherChartLines(arrays, config.glowColor)

                val leftAxisLabels = config.leftAxis?.let { chartScope.gatherLeftAxisLabels(it) }
                val rightAxisLabels = config.rightAxis?.let { chartScope.gatherRightAxisLabels(it) }
                val bottomAxisLabels = config.bottomAxis?.let { chartScope.gatherBottomAxisLabels(it) }

                val horizontalRule = config.leftAxis?.let { chartScope.gatherHorizontaRule(it, config.rightAxis) }

                onDrawBehind {
                    horizontalRule?.let { drawHorizontalRule(it, chartScope, animation) }

                    leftAxisLabels?.let { drawAxisLabels(it, animation) }

                    rightAxisLabels?.let { drawAxisLabels(it, animation) }

                    bottomAxisLabels?.let { drawAxisLabels(it, animation) }

                    drawChartLines(lines, animation)

                    drawChartPoints(lines, chartScope, config, animation)
                }
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val pos = event.changes.first().position
                        println(pos)
                        // pos.x and pos.y be yer coords
                    }
                }
            }
    )
}

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
    val isAnimated: Boolean = true,
    val contentColor: Color = Color.White,
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