package pondui.ui.charts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import kabinet.utils.toMetricString

@Stable
@Immutable
data class ChartAxis(
    val values: List<AxisValue>,
    val color: Color,
    val dimension: ChartDimension,
    val maxLabelWidthPx: Float,
)

data class AxisValue(
    val value: Float,
    val label: String,
    val layout: TextLayoutResult
)

internal data class ChartAxisLine(
    val start: Offset,
    val end: Offset,
    val brush: Brush,
)

fun <T> gatherSideAxis(
    side: AxisSide,
    arrays: List<ChartArray<T>>,
    dimensionsY: List<ChartDimension>,
    textRuler: TextMeasurer,
    labelFontSize: TextUnit
) = arrays.withIndex().firstOrNull { it.value.axis?.side == side }?.let { indexedArray ->
    val (index, array) = indexedArray
    val axisConfig = array.axis!!
    val dimension = dimensionsY[index]
    gatherAxis(axisConfig, dimension, textRuler, array.color, labelFontSize)
}

fun gatherAxis(
    axisConfig: AxisConfig,
    dimension: ChartDimension,
    textRuler: TextMeasurer,
    color: Color,
    fontSize: TextUnit
): ChartAxis {
    val values = (0 until axisConfig.tickCount).map { tickIndex ->
        val value = tickIndex * (dimension.range / (axisConfig.tickCount - 1)) + dimension.min
        val label = axisConfig.toLabel(value)
        val layout = textRuler.measure(
            text = label,
            style = TextStyle(color = color, fontSize = fontSize)
        )
        AxisValue(value, label, layout)
    }
    val maxLabelWidthPx = values.maxOf { it.layout.size.width }.toFloat()
    return ChartAxis(
        values = values,
        color = color,
        dimension = dimension,
        maxLabelWidthPx = maxLabelWidthPx
    )
}