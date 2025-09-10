package pondui.ui.charts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import kabinet.utils.toMetricString

const val CHART_POINTER_TARGET_DISTANCE = 30

const val CHART_BOTTOM_MARGIN = 24
const val CHART_AXIS_LABEL_HEIGHT = 12
const val CHART_AXIS_PADDING = CHART_AXIS_LABEL_HEIGHT / 2

interface ChartConfig {
    val contentColor: Color
    val isAnimated: Boolean
    val glowColor: Color?
    val bottomAxis: AxisConfig.Bottom?
    val startX: Double?
    val endX: Double?
    val provideLabelX: (Double) -> String
}

data class DataScope(
    val maxX: Double,
    val minX: Double,
    val maxY: Double,
    val minY: Double,
) {
    val rangeX get() = maxX - minX
    val rangeY get() = maxY - minY
}

data class ChartDimension(
    val scalePx: Float,
    val max: Double,
    val min: Double
) {
    val range get() = (max - min).toFloat()
}

internal fun <T> gatherDataScope(
    array: List<T>,
    floor: Double?,
    ceiling: Double?,
    startX: Double?,
    endX: Double?,
    provideX: ((T) -> Double)?,
    provideY: (T) -> Double
): DataScope {
    var minX = startX ?: Double.MAX_VALUE
    var maxX = endX ?: Double.MIN_VALUE
    var minY = floor ?: Double.MAX_VALUE
    var maxY = ceiling ?: Double.MIN_VALUE
    array.forEachIndexed { index, value ->
        val x = provideX?.invoke(value) ?: index.toDouble()
        val y = provideY(value)
        if (x < minX) minX = x
        if (x > maxX) maxX = x
        if (y < minY) minY = y
        if (y > maxY) maxY = y
    }
    return DataScope(
        maxX = maxX,
        minX = minX,
        maxY = maxY,
        minY = minY,
    )
}