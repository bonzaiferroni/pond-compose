package pondui.ui.charts

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp

internal fun DrawScope.drawFocusValue(
    chartScope: ChartScope,
    pointerTarget: ChartPoint?,
    pointerTargetPrev: ChartPoint?,
    pointerAnimation: Float,
) {
    pointerTarget?.let { target ->
        val labelLayoutY = chartScope.textRuler.measure(
            text = target.labelY,
            style = TextStyle(color = target.color, fontSize = chartScope.labelFontSize)
        )
        val x = when (target.side) {
            AxisSide.Left -> (chartScope.leftAxis?.maxLabelWidthPx ?: labelLayoutY.size.width.toFloat()) / 2 - labelLayoutY.size.width / 2f
            AxisSide.Right -> chartScope.sizePx.width - (chartScope.rightAxis?.maxLabelWidthPx ?: labelLayoutY.size.width.toFloat()) / 2 - labelLayoutY.size.width / 2f
        }
        drawText(labelLayoutY, topLeft = Offset(
            x = x,
            y = target.offset.y - labelLayoutY.size.height / 2f,
        ), alpha = pointerAnimation)
    }
}