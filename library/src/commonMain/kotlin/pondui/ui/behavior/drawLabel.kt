package pondui.ui.behavior

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
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

@Composable
fun Modifier.drawLabel(
    label: String,
    color: Color = Pond.colors.primary,
    alignment: Alignment.Horizontal = Alignment.End
): Modifier {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = Pond.typo.body.copy(fontSize = 12.sp)
    val labelResult = remember { textMeasurer.measure(
        text = label,
        style = labelStyle
    )}
    val extraTopPadding = with(density) { labelResult.size.height.toDp() / 2 }

    return drawWithContent {
        drawContent()
        val labelBgCorner = CornerRadius(extraTopPadding.toPx() * 2)
        val labelBgPadding = 4.dp.toPx()
        val labelBgOffsetX = labelBgPadding * 4
        val labelOffsetX = when (alignment) {
            Alignment.End -> size.width - labelResult.size.width - labelBgOffsetX
            Alignment.Start -> labelBgOffsetX
            Alignment.CenterHorizontally -> size.width / 2 - labelResult.size.width / 2
            else -> error("unsupported label alignment")
        }
        drawRoundRect(
            color = color,
            cornerRadius = labelBgCorner,
            topLeft = Offset(x = labelOffsetX - labelBgPadding, y = 0f),
            size = Size(
                width = labelResult.size.width + labelBgPadding * 2,
                height = labelResult.size.height.toFloat()
            )
        )
        drawText(
            textLayoutResult = labelResult,
            color = Color.White,
            topLeft = Offset(x = labelOffsetX, y = 0f)
        )
    }.padding(top = extraTopPadding)
}