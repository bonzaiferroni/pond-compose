package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.style.TextAlign
import pondui.ui.core.PondApp
import pondui.utils.changeFocusWithTab
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors
import pondui.utils.darken

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
    ProvideSkyColors {
        val color = Pond.localColors.content
        Box(modifier = Modifier.height(IntrinsicSize.Max)) {
            BasicTextField(
                value = text,
                onValueChange = { if (!it.contains('\t')) onTextChange(it) },
                textStyle = TextStyle(color = color),
                cursorBrush = SolidColor(color),
                minLines = minLines,
                modifier = modifier.background(Pond.colors.textField)
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
                    text = placeholder.uppercase(),
                    color = Pond.localColors.contentDim,
                    style = TextStyle(fontSize = Pond.typo.label.fontSize, textAlign = TextAlign.Center),
                    modifier = Modifier.fillMaxHeight()
                        .padding(Pond.ruler.halfPadding)
                )
            }
        }
    }
}