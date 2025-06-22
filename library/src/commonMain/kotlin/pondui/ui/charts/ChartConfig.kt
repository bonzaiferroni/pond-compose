package pondui.ui.charts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

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
    val bottomAxis: AxisConfig.Bottom? = null,
)

@Stable
@Immutable
data class ChartArray<T>(
    val values: List<T>,
    val color: Color,
    val label: String? = null,
    val isBezier: Boolean = true,
    val axis: AxisConfig.Side ? = null,
    val floor: Double? = null,
    val ceiling: Double? = null,
    val provideY: (T) -> Double,
)

data class DataScope(
    val maxX: Double,
    val minX: Double,
    val maxY: Double,
    val minY: Double,
) {
    val rangeX get() = maxX - minX
    val rangeY get() = maxY - minY
}

internal fun <T> gatherDataScope(
    array: List<T>,
    floor: Double?,
    ceiling: Double?,
    provideX: (T) -> Double,
    provideY: (T) -> Double
): DataScope {
    var minX = Double.MAX_VALUE
    var maxX = Double.MIN_VALUE
    var minY = Double.MAX_VALUE
    var maxY = Double.MIN_VALUE
    for (value in array) {
        val x = provideX(value)
        val y = provideY(value)
        if (x < minX) minX = x
        if (x > maxX) maxX = x
        if (y < minY) minY = y
        if (y > maxY) maxY = y
    }
    return DataScope(
        maxX = maxX,
        minX = minX,
        maxY = ceiling ?: maxY,
        minY = floor ?: minY,
    )
}