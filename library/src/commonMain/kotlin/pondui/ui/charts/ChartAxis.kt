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
import pondui.ui.controls.AxisTick

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

fun <T> gatherSideAutoAxis(
    side: AxisSide,
    arrays: List<ChartArray<T>>,
    dimensionsY: List<ChartDimension>,
    textRuler: TextMeasurer,
    labelFontSize: TextUnit
) = arrays.withIndex().firstOrNull { it.value.axis?.side == side }?.let { indexedArray ->
    val (index, array) = indexedArray
    val axisConfig = array.axis!!
    val dimension = dimensionsY[index]
    gatherAutoAxis(axisConfig, dimension, textRuler, array.color, labelFontSize)
}

fun gatherAutoAxis(
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

fun gatherAxis(
    ticks: List<AxisTick>,
    dimension: ChartDimension,
    textRuler: TextMeasurer,
    color: Color,
    labelFontSize: TextUnit
): ChartAxis {
    val values = ticks.map { tick ->
        val value = tick.value + dimension.min
        val layout = textRuler.measure(
            text = tick.label,
            style = TextStyle(color = color, fontSize = labelFontSize)
        )
        AxisValue(value, tick.label, layout)
    }
    val maxLabelWidthPx = values.maxOf { it.layout.size.width }.toFloat()
    return ChartAxis(
        values = values,
        color = color,
        dimension = dimension,
        maxLabelWidthPx = maxLabelWidthPx
    )
}