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
    chartAxis: ChartAxis,
    gatherLabelPoint: ChartScope.(AxisValue, Float) -> Offset
) = chartAxis.values.map { axisValue ->
    ChartAxisLabel(
        layoutResult = axisValue.layout,
        point = gatherLabelPoint(axisValue, chartAxis.maxLabelWidthPx)
    )
}

internal fun ChartScope.gatherLeftAxisLabels() = this.leftAxis?.let { chartAxis ->
    gatherAxisLabels(chartAxis) { axisValue, maxLayoutWidth ->
        val minY = chartAxis.dimension.min
        val scaleY = chartAxis.dimension.scalePx
        Offset(
            x = maxLayoutWidth / 2,
            y = sizePx.height - chartMinY - (axisValue.value - minY) * scaleY
        )
    }
}

internal fun ChartScope.gatherRightAxisLabels() = this.rightAxis?.let { chartAxis ->
    gatherAxisLabels(chartAxis) { axisValue, maxLayoutWidth ->
        val minY = chartAxis.dimension.min
        val scaleY = chartAxis.dimension.scalePx
        Offset(
            x = sizePx.width - maxLayoutWidth / 2,
            y = sizePx.height - chartMinY - (axisValue.value - minY) * scaleY
        )
    }
}

internal fun ChartScope.gatherBottomAxisLabels() = this.bottomAxis?.let { chartAxis ->
    gatherAxisLabels(chartAxis) { axisValue, maxLayoutWidth ->
        val minX = chartAxis.dimension.min
        val scaleX = chartAxis.dimension.scalePx
        Offset(
            x = chartMinX + (axisValue.value - minX) * scaleX,
            y = sizePx.height - axisValue.layout.size.height / 2f
        )
    }
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