package pondui.ui.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import pondui.ui.modifiers.animate
import pondui.ui.modifiers.animateFloat
import pondui.ui.theme.Pond

@Composable
fun ButtonToggle(
    value: Boolean,
    onToggle: (Boolean) -> Unit,
    color : Color = Pond.colors.accent,
    content: @Composable () -> Unit,
) {
    val outlineColor = Pond.localColors.content.copy(alpha = .5f)
    val scale by animateFloat(if (value) 1f else 0f)
    val shape = Pond.ruler.pill
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .clip(shape)
            .clickable { onToggle(!value) }
            .drawBehind {
                val outline = shape.createOutline(size, layoutDirection = LayoutDirection.Ltr, density = density)
                drawOutline(
                    outline = outline,
                    color = outlineColor,
                    style = Stroke(width = 4.dp.toPx()) // 4.dp wide stroke
                )
                withTransform({
                    scale(scale, scale, pivot = center) // Scale from the center
                }) {
                    drawRoundRect(
                        color = color,
                        cornerRadius = CornerRadius(16.dp.toPx()),
                    )
                }
            }
            .padding(Pond.ruler.doublePadding)
    ) {
        content()
    }
}

@Composable
fun ButtonToggle(
    value: Boolean,
    text: String,
    color : Color = Pond.colors.accent,
    onToggle: (Boolean) -> Unit,
) {
    ButtonToggle(value, onToggle, color) {
        val color = when (value) {
            true -> Pond.colors.contentSky
            false -> Pond.localColors.content
        }.animate()
        Text(text = text.uppercase(), style = Pond.typo.small, color = color)
    }
}