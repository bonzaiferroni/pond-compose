package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pondui.ui.behavior.changeFocusWithTab
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors

@Composable
fun TextField(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.primary,
    label: String? = null,
    placeholder: String? = null,
    hideCharacters: Boolean = false,
    initialSelectAll: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    onTextChanged: (String) -> Unit,
) {
    var value by remember {
        mutableStateOf(
            TextFieldValue(
                text = text,
                selection = if (initialSelectAll) TextRange(0, text.length) else TextRange(text.length)
            )
        )
    }

    LaunchedEffect(text) {
        if (text == value.text) return@LaunchedEffect
        value = value.copy(text)
    }

    val density = LocalDensity.current
    val background = Pond.colors.void
    val corner = Pond.ruler.unitCorner
    val textPadding = Pond.ruler.doubleSpacing
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = Pond.typo.body.copy(fontSize = 12.sp)
    val extraTopPadding = if (label != null) with(density) { labelStyle.fontSize.toDp() / 2 } else 0.dp

    var isFocused by remember { mutableStateOf(false) }
    ProvideSkyColors {
        val textColor = Pond.localColors.content
        BasicTextField(
            value = value,
            onValueChange = {
                if (!it.text.contains('\t')) {
                    value = it
                    onTextChanged(it.text)
                }
            },
            textStyle = TextStyle(color = textColor),
            cursorBrush = SolidColor(textColor),
            minLines = minLines,
            maxLines = maxLines,
            modifier = modifier.width(IntrinsicSize.Min)
                .defaultMinSize(150.dp) // huh?
                .drawWithCache {
                    val extraTopPaddingPx = extraTopPadding.toPx()
                    val topLeft = Offset(0f, extraTopPaddingPx)
                    val cornerRadius = CornerRadius(corner.toPx())
                    val labelResult = label?.let {
                        textMeasurer.measure(
                            text = it,
                            style = labelStyle
                        )
                    }
                    onDrawBehind {
                        drawRoundRect(
                            color = background,
                            topLeft = topLeft,
                            cornerRadius = cornerRadius
                        )

                        labelResult?.let {
                            val labelBgCorner = CornerRadius(extraTopPadding.toPx() * 2)
                            val labelBgPadding = 4.dp.toPx()
                            val labelOffsetX = size.width - labelResult.size.width - labelBgPadding * 4
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
                        }
                    }
                }
                .padding(
                    start = textPadding, end = textPadding, bottom = textPadding,
                    top = textPadding + extraTopPadding,
                )
                .onFocusChanged { isFocused = it.isFocused }
                .onKeyEvent { isFocused && it.key != Key.Tab }
                .changeFocusWithTab(),
            visualTransformation = when (hideCharacters) {
                true -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
        ) { innerTextField ->
            innerTextField()
            if (placeholder != null && text.isEmpty() && !isFocused) {
                Text(
                    text = placeholder.uppercase(),
                    color = Pond.localColors.contentDim,
                    style = TextStyle(fontSize = Pond.typo.label.fontSize),
                )
            }
        }
    }
}