package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import pondui.ui.behavior.changeFocusWithTab
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors

@Composable
fun TextField(
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String? = null,
    hideCharacters: Boolean = false,
    initialSelectAll: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
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

    var isFocused by remember { mutableStateOf(false) }
    ProvideSkyColors {
        val color = Pond.localColors.content
        BasicTextField(
            value = value,
            onValueChange = {
                if (!it.text.contains('\t')) {
                    value = it
                    onTextChange(it.text)
                }
            },
            textStyle = TextStyle(color = color),
            cursorBrush = SolidColor(color),
            minLines = minLines,
            maxLines = maxLines,
            modifier = modifier.width(IntrinsicSize.Min)
                .clip(Pond.ruler.unitCorners)
                .defaultMinSize(150.dp)
                .background(Pond.colors.textField)
                .padding(Pond.ruler.doublePadding)
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