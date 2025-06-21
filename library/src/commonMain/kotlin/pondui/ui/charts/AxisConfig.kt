package pondui.ui.charts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import kabinet.utils.toMetricString

@Stable
@Immutable
data class AxisConfig(
    val values: List<AxisValue>,
    val color: Color,
    val dimension: ChartDimension,
)

data class AxisValue(
    val value: Float,
    val label: String = value.toInt().toString()
)

enum class VerticalAxis {
    Left,
    Right,
}

fun <T> ChartScope.gatherVerticalAxis(
    arrays: List<ChartArray<T>>,
    axis: VerticalAxis,
) = arrays.withIndex().firstOrNull { it.value.axis == axis }?.let {
    val (index, array) = it
    val dimension = this.dimensionsY[index]
    val minY = dimension.min
    val maxY = dimension.max
    val midY = dimension.range / 2 + minY
    AxisConfig(
        values = listOf(
            AxisValue(minY, minY.toMetricString()),
            AxisValue(midY, midY.toMetricString()),
            AxisValue(maxY, maxY.toMetricString())
        ),
        color = array.color,
        dimension = dimension
    )
}