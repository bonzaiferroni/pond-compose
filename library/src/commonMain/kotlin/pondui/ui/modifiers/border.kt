package pondui.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.topBorder(color: Color, width: Dp = 1.dp) = drawBehind {
    val strokeWidth = width.toPx()
    drawLine(
        color = color,
        start = Offset(0f, strokeWidth / 2),
        end = Offset(size.width, strokeWidth / 2),
        strokeWidth = strokeWidth
    )
}