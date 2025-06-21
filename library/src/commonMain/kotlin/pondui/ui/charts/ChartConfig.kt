package pondui.ui.charts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import kabinet.utils.toMetricString

const val CHART_POINTER_TARGET_DISTANCE = 30

const val CHART_SIDE_AXIS_MARGIN = 24
const val CHART_AXIS_LABEL_HEIGHT = 12
const val CHART_AXIS_PADDING = CHART_AXIS_LABEL_HEIGHT / 2

@Stable
@Immutable
data class ChartConfig(
    val contentColor: Color,
    val isAnimated: Boolean = true,
    val glowColor: Color? = null,
    val bottomAxis: BottomAxisConfig? = null,
)

@Stable
@Immutable
data class ChartArray<T>(
    val values: List<T>,
    val color: Color,
    val provideX: (T) -> Float,
    val provideY: (T) -> Float,
    val scope: DataScope? = null,
    val label: String? = null,
    val isBezier: Boolean = true,
    val axis: SideAxisConfig ? = null
)

interface AxisConfig {
    val tickCount: Int
    val toLabel: (Float) -> String

    interface Side: AxisConfig {
        val side: AxisSide
    }

    interface Bottom: AxisConfig
}

data class SideAxisConfig(
    override val tickCount: Int,
    override val side: AxisSide,
    override val toLabel: (Float) -> String = { it.toMetricString() }
): AxisConfig.Side

data class BottomAxisConfig(
    override val tickCount: Int,
    override val toLabel: (Float) -> String = { it.toMetricString() }
): AxisConfig.Bottom

enum class AxisSide {
    Left,
    Right,
}

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

internal fun <T> gatherDataScope(array: List<T>, provideX: (T) -> Float, provideY: (T) -> Float): DataScope {
    var xMin = Float.MAX_VALUE
    var xMax = Float.MIN_VALUE
    var yMin = Float.MAX_VALUE
    var yMax = Float.MIN_VALUE
    for (value in array) {
        val x = provideX(value)
        val y = provideY(value)
        if (x < xMin) xMin = x
        if (x > xMax) xMax = x
        if (y < yMin) yMin = y
        if (y > yMax) yMax = y
    }
    return DataScope(
        maxX = xMax,
        minX = xMin,
        maxY = yMax,
        minY = yMin,
    )
}