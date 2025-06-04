package pondui.ui.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import compose.icons.tablericons.X
import pondui.ui.behavior.filterKeyPress
import pondui.ui.behavior.magic
import pondui.ui.behavior.ifTrue
import pondui.ui.behavior.onEnterPressed
import pondui.ui.theme.Pond

@Composable
fun EditText(
    text: String,
    style: TextStyle = Pond.typo.body,
    isEditable: Boolean = true,
    initialSelectAll: Boolean = true,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    modifier: Modifier = Modifier,
    onAcceptEdit: (String) -> Unit,
) {
    var isEditing by remember { mutableStateOf(false) }
    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = text,
                selection = if (initialSelectAll) TextRange(0, text.length) else TextRange(text.length)
            )
        )
    }

    fun acceptEdit(value: String) {
        onAcceptEdit(value)
        isEditing = false
    }

    fun cancelEdit(value: String) {
        fieldValue = fieldValue.copy(value)
        isEditing = false
    }

    LaunchedEffect(isEditable) {
        if (!isEditable) cancelEdit(text)
    }

    LaunchedEffect(text) {
        if (text == fieldValue.text) return@LaunchedEffect
        fieldValue = fieldValue.copy(text)
        isEditing = false
    }

    val backgroundColor by animateColorAsState(if (isEditing) Pond.colors.void.copy(.2f) else Color.Transparent)
    val cornerRadius = Pond.ruler.unitCorner

    Box(
        modifier = modifier.ifTrue(!isEditing && isEditable) { clickable { isEditing = true } }
            .drawBehind {
                drawRoundRect(
                    color = backgroundColor,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
    ) {
        if (isEditing) {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }

            BasicTextField(
                value = fieldValue,
                onValueChange = { fieldValue = it },
                modifier = Modifier.width(IntrinsicSize.Min)
                    .focusRequester(focusRequester)
                    .filterKeyPress(Key.Tab)
                    .onEnterPressed { acceptEdit(fieldValue.text) },
                textStyle = style.copy(color = color),
                maxLines = maxLines
            )
        } else {
            Text(fieldValue.text, color, style, maxLines, overflow, Modifier)
        }

        val offset = with(LocalDensity.current) { 32.dp.toPx().toInt() }
        Popup(
            alignment = Alignment.CenterStart,
            offset = IntOffset(-offset, 0)
        ) {
            Button(
                TablerIcons.X,
                background = Pond.colors.tertiary,
                modifier = Modifier
                    .magic(isEditing, offsetX = offset)
            ) { cancelEdit(text) }
        }

        Popup(
            alignment = Alignment.CenterEnd,
            offset = IntOffset(offset, 0)
        ) {
            Button(
                TablerIcons.Check,
                modifier = Modifier
                    .magic(isEditing, offsetX = -offset)
            ) { acceptEdit(fieldValue.text) }
        }
    }
}