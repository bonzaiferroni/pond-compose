package pondui.ui.behavior

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.controls.actionable
import pondui.ui.theme.Pond
import pondui.utils.lighten

@Composable
fun Modifier.selected(
    isSelected: Boolean,
    color: Color = Pond.colors.selected,
    stroke: Dp = 2.dp,
    padding: Dp = stroke,
    radius: Dp = Pond.ruler.unitCorner,
): Modifier {
    val factor by animateFloatAsState(if (isSelected) 1f else 0f, spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ))

    return this.drawBehind {
        val strokePx = stroke.toPx()
        val radiusPx = radius.toPx()
        // draw only the border
        val sizeFactor = (10 - 10 * factor).dp.toPx()
        val paddingPx = padding.toPx()

        drawRoundRect(
            color = color.copy(.1f * factor),
            topLeft = Offset(sizeFactor / 2 - paddingPx, sizeFactor / 2 - paddingPx),
            size = Size(size.width - sizeFactor + paddingPx * 2, size.height - sizeFactor + paddingPx * 2),
            cornerRadius = CornerRadius(radiusPx, radiusPx),
        )

        drawRoundRect(
            color = color.copy(factor).lighten(.2f * factor),
            topLeft = Offset(strokePx / 2 + sizeFactor / 2 - paddingPx, strokePx / 2 + sizeFactor / 2 - paddingPx),
            size = Size(size.width - strokePx - sizeFactor + paddingPx * 2, size.height - strokePx - sizeFactor + paddingPx * 2),
            cornerRadius = CornerRadius(radiusPx, radiusPx),
            style = Stroke(width = strokePx)
        )
    }
}

@Composable
fun Modifier.selectable(
    onSelectedChange: (Boolean) -> Unit
): Modifier {
    var isSelected by remember { mutableStateOf(false) }

    return this.actionable {
        selectedAction?.invoke()
        selectedAction = {
            isSelected = false
            onSelectedChange(false)
        }
        isSelected = true
        onSelectedChange(true)
    }.selected(isSelected)
}

private var selectedAction: (() -> Unit)? = null