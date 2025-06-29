package pondui.ui.behavior

import androidx.compose.animation.core.EaseInOutElastic
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.theme.Pond
import pondui.utils.lighten

@Composable
fun Modifier.selected(
    isSelected: Boolean,
    stroke: Dp = 2.dp,
    padding: PaddingValues = PaddingValues(stroke * 3),
    radius: Dp = Pond.ruler.midCorner,
): Modifier {
    val factor by animateFloatAsState(if (isSelected) 1f else 0f, spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ))

    val color = Pond.colors.selected
    return this.drawBehind {
        val strokePx = stroke.toPx()
        val radiusPx = radius.toPx()
        // draw only the border
        val sizeFactor = (10 - 10 * factor).dp.toPx()

        drawRoundRect(
            color = color.copy(.1f * factor),
            topLeft = Offset(sizeFactor / 2, sizeFactor / 2),
            size = Size(size.width - sizeFactor, size.height - sizeFactor),
            cornerRadius = CornerRadius(radiusPx, radiusPx),
        )

        drawRoundRect(
            color = color.copy(factor).lighten(.2f * factor),
            topLeft = Offset(strokePx + sizeFactor / 2, strokePx + sizeFactor / 2),
            size = Size(size.width - strokePx * 2 - sizeFactor, size.height - strokePx * 2 - sizeFactor),
            cornerRadius = CornerRadius(radiusPx, radiusPx),
            style = Stroke(width = strokePx)
        )
    }.padding(padding)
}