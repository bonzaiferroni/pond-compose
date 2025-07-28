package pondui.ui.behavior

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

fun Modifier.outline(color: Color, corner: Dp, stroke: Dp) = this.drawBehind { drawOutline(color, corner, stroke) }

fun DrawScope.drawOutline(
    color: Color,
    corner: Dp,
    stroke: Dp,
) {
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(corner.toPx()),
        style = Stroke(stroke.toPx())
    )
}