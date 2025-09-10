package pondui.ui.charts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText

internal fun <T> DrawScope.drawFocusValue(
    lineChartScope: LineChartScope,
    pointerTarget: ChartPoint<T>?,
    pointerTargetPrev: ChartPoint<T>?,
    pointerAnimation: Float,
) {
    pointerTarget?.let { target ->
        val labelLayoutY = lineChartScope.textRuler.measure(
            text = target.labelY,
            style = TextStyle(color = target.color, fontSize = lineChartScope.labelFontSize)
        )
        val x = when (target.side) {
            AxisSide.Left -> (lineChartScope.leftAxis?.maxLabelWidthPx ?: labelLayoutY.size.width.toFloat()) / 2 - labelLayoutY.size.width / 2f
            AxisSide.Right -> lineChartScope.sizePx.width - (lineChartScope.rightAxis?.maxLabelWidthPx ?: labelLayoutY.size.width.toFloat()) / 2 - labelLayoutY.size.width / 2f
        }
        drawText(labelLayoutY, topLeft = Offset(
            x = x,
            y = target.offset.y - labelLayoutY.size.height / 2f,
        ), alpha = pointerAnimation)
    }
}