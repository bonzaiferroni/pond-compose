package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
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
import pondui.ui.behavior.drawLabel
import pondui.ui.behavior.ifNotNull
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors
import pondui.utils.glowWith
import pondui.utils.mixWith

@Composable
fun TextField(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.void,
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

    val background = color.mixWith(Pond.colors.void, .8f)
    val corner = Pond.ruler.unitCorner

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
                .ifNotNull(label) { drawLabel(it, color, addPadding = true) }
                .drawBehind {
                    drawRoundRect(
                        color = background,
                        cornerRadius = CornerRadius(corner.toPx())
                    )
                }
                .padding(Pond.ruler.doubleSpacing)
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