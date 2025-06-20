package pondui.ui.charts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText

internal data class ChartAxisLabel(
    val layoutResult: TextLayoutResult,
    val topLeft: Offset
)

internal fun ChartScope.gatherAxisLabels(
    axisConfig: AxisConfig,
    gatherOffset: ChartScope.(AxisValue, TextLayoutResult) -> Offset
) = axisConfig.values.mapIndexed { i, axisValue ->
    val result = textRuler.measure(
        text = axisValue.label,
        style = TextStyle(color = axisConfig.color, fontSize = labelFontSize)
    )
    ChartAxisLabel(
        layoutResult = result,
        topLeft = gatherOffset(axisValue, result)
    )
}

internal fun ChartScope.gatherLeftAxisLabels(
    axisConfig: AxisConfig
) = gatherAxisLabels(axisConfig) { axisValue, textLayoutResult ->
    Offset(
        x = 0f,
        y = sizePx.height - axisPaddingPx - axisValue.value * scaleY - textLayoutResult.size.height / 2
    )
}

internal fun ChartScope.gatherRightAxisLabels(
    axisConfig: AxisConfig
) = gatherAxisLabels(axisConfig) { axisValue, textLayoutResult ->
    Offset(
        x = sizePx.width - textLayoutResult.size.width,
        y = sizePx.height - axisPaddingPx - axisValue.value * scaleY - textLayoutResult.size.height / 2
    )
}

internal fun ChartScope.gatherBottomAxisLabels(
    axisConfig: AxisConfig
) = gatherAxisLabels(axisConfig) { axisValue, textLayoutResult ->
    Offset(
        x = chartMinX + axisValue.value * scaleX - textLayoutResult.size.width / 2f,
        y = sizePx.height - textLayoutResult.size.height
    )
}

internal fun DrawScope.drawAxisLabels(
    labels: List<ChartAxisLabel>,
    animation: Float,
) = labels.forEachIndexed { i, label ->
    val alpha = -i + animation * labels.size
    drawText(
        textLayoutResult = label.layoutResult,
        topLeft = label.topLeft,
        alpha = alpha
    )
}