package pondui.ui.charts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText

internal data class ChartAxisLabel(
    val layoutResult: TextLayoutResult,
    val point: Offset,
)

internal fun ChartScope.gatherAxisLabels(
    axisConfig: AxisConfig,
    gatherLabelPoint: ChartScope.(AxisValue, TextLayoutResult, Float) -> Offset
): List<ChartAxisLabel> {

    val pairs = axisConfig.values.map { axisValue ->
        val result = textRuler.measure(
            text = axisValue.label,
            style = TextStyle(color = axisConfig.color, fontSize = labelFontSize)
        )
        Pair(axisValue, result)
    }

    val maxLayoutWidth = pairs.maxOf { it.second.size.width }.toFloat()

    return pairs.map {
        val (axisValue, layoutResult) = it
        ChartAxisLabel(
            layoutResult = layoutResult,
            point = gatherLabelPoint(axisValue, layoutResult, maxLayoutWidth)
        )
    }
}

internal fun ChartScope.gatherLeftAxisLabels(
    axisConfig: AxisConfig
) = gatherAxisLabels(axisConfig) { axisValue, textLayoutResult, maxLayoutWidth ->
    val minY = axisConfig.dimension.dataScope.minY
    val scaleY = axisConfig.dimension.scaleY
    Offset(
        x = maxLayoutWidth / 2,
        y = sizePx.height - chartMinY - (axisValue.value - minY) * scaleY
    )
}

internal fun ChartScope.gatherRightAxisLabels(
    axisConfig: AxisConfig
) = gatherAxisLabels(axisConfig) { axisValue, textLayoutResult, maxLayoutWidth ->
    val minY = axisConfig.dimension.dataScope.minY
    val scaleY = axisConfig.dimension.scaleY
    Offset(
        x = sizePx.width - maxLayoutWidth / 2,
        y = sizePx.height - chartMinY - (axisValue.value - minY) * scaleY
    )
}

internal fun ChartScope.gatherBottomAxisLabels(
    axisConfig: AxisConfig
) = gatherAxisLabels(axisConfig) { axisValue, textLayoutResult, maxLayoutWidth ->
    Offset(
        x = chartMinX + axisValue.value * scaleX,
        y = sizePx.height - textLayoutResult.size.height / 2f
    )
}

internal fun DrawScope.drawAxisLabels(
    labels: List<ChartAxisLabel>,
    animation: Float,
) = labels.forEachIndexed { i, label ->
    val alpha = -i + animation * labels.size
    drawText(
        textLayoutResult = label.layoutResult,
        topLeft = Offset(
            x = label.point.x - label.layoutResult.size.width / 2f,
            y = label.point.y - label.layoutResult.size.height / 2f
        ),
        alpha = alpha,
    )
}