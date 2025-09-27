package pondui.ui.controls

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.modifiers.changeFocusWithTab
import pondui.ui.modifiers.drawLabel
import pondui.ui.modifiers.ifNotNull
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors
import pondui.utils.mixWith

@Composable
fun TextField(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.void,
    textAlign: TextAlign = TextAlign.Start,
    label: String? = null,
    placeholder: String? = null,
    hideCharacters: Boolean = false,
    initialSelectAll: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    minWidth: Dp = 150.dp,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    onChange: (String) -> Unit,
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

    val background = color.mixWith(Pond.colors.void, .8f)
    val corner = Pond.ruler.unitCorner

    var isFocused by remember { mutableStateOf(false) }
    LaunchedEffect(isFocused) {
        onFocusChanged?.invoke(isFocused)
    }

    ProvideSkyColors {
        val textColor = Pond.localColors.content
        BasicTextField(
            value = value,
            onValueChange = {
                if (!it.text.contains('\t')) {
                    value = it
                    onChange(it.text)
                }
            },
            textStyle = Pond.typo.body.copy(color = textColor, textAlign = textAlign),
            cursorBrush = SolidColor(textColor),
            minLines = minLines,
            maxLines = maxLines,
            visualTransformation = when (hideCharacters) {
                true -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            modifier = modifier.width(IntrinsicSize.Min)
                .widthIn(min = minWidth)
                .ifNotNull(label) { drawLabel(it, color) }
                .drawBehind {
                    drawRoundRect(
                        color = background,
                        cornerRadius = CornerRadius(corner.toPx())
                    )
                }
                .padding(Pond.ruler.doubleSpacing)
                .onFocusChanged { isFocused = it.isFocused }
                .onKeyEvent { isFocused && it.key != Key.Tab }
                .changeFocusWithTab()
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