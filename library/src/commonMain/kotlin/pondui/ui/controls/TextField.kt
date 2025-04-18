package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import pondui.utils.changeFocusWithTab
import pondui.ui.theme.Pond

@Composable
fun TextField(
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String? = null,
    hideCharacters: Boolean = false,
    minLines: Int = 1,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val color = Pond.colors.contentSky
    Box {
        BasicTextField(
            value = text,
            onValueChange = { if (!it.contains('\t')) onTextChange(it) },
            textStyle = TextStyle(color = color),
            cursorBrush = SolidColor(color),
            minLines = minLines,
            modifier = modifier.background(Pond.colors.primary.copy(.75f))
                .padding(Pond.ruler.halfPadding)
                .onFocusChanged { isFocused = it.isFocused }
                .onKeyEvent { isFocused && it.key != Key.Tab }
                .changeFocusWithTab(),
            visualTransformation = when (hideCharacters) {
                true -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
        )
        if (placeholder != null && text.isEmpty() && !isFocused) {
            Text(
                text = placeholder,
                color = Pond.localColors.contentDim,
                modifier = Modifier.padding(Pond.ruler.halfPadding)
            )
        }
    }
}