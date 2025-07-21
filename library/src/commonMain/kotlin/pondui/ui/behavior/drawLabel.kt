package pondui.ui.behavior

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pondui.ui.theme.Pond
import pondui.utils.mixWith

@Composable
fun Modifier.drawLabel(
    label: String,
    color: Color = Pond.colors.void,
    addPadding: Boolean = false,
    alignX: AlignX = AlignX.End,
    alignY: AlignY = AlignY.Top,
): Modifier {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = Pond.typo.body.copy(fontSize = 12.sp, color = Pond.colors.contentSky.copy(.8f))
    val labelResult = remember {
        textMeasurer.measure(
            text = label,
            style = labelStyle
        )
    }
    val labelHeight = with(density) { labelResult.size.height.toDp() }
    val bgColor = color.mixWith(Pond.colors.void)
    val unitSpacing = Pond.ruler.unitSpacing

    return ifTrue(addPadding) {
        when (alignY) {
            AlignY.Top -> padding(top = labelHeight / 2)
            AlignY.Middle -> this
            AlignY.Bottom -> padding(bottom = labelHeight / 2)
        }
    }
        .drawWithContent {
            drawContent()
            val labelHeightPx = labelHeight.toPx()
            val labelBgCorner = CornerRadius(labelHeightPx)
            val labelBgPadding = unitSpacing.toPx()
            val labelBgOffsetX = labelBgPadding * 2
            val labelOffsetX = when (alignX) {
                AlignX.End -> size.width - labelResult.size.width - labelBgOffsetX
                AlignX.Start -> labelBgOffsetX
                AlignX.Center -> size.width / 2 - labelResult.size.width / 2
            }
            val labelOffsetY = when (alignY) {
                AlignY.Top -> -labelHeightPx / 2
                AlignY.Middle -> size.height / 2 - labelHeightPx / 2
                AlignY.Bottom -> size.height - labelHeightPx / 2
            }
            drawRoundRect(
                color = bgColor,
                cornerRadius = labelBgCorner,
                topLeft = Offset(x = labelOffsetX - labelBgPadding, y = labelOffsetY),
                size = Size(
                    width = labelResult.size.width + labelBgPadding * 2,
                    height = labelResult.size.height.toFloat()
                )
            )
            drawText(
                textLayoutResult = labelResult,
                topLeft = Offset(x = labelOffsetX, y = labelOffsetY)
            )
        }
}

sealed interface Align
enum class AlignX: Align {
    Start,
    Center,
    End
}
enum class AlignY: Align {
    Top,
    Middle,
    Bottom,
}