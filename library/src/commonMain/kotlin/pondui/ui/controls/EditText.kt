package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import compose.icons.tablericons.X
import pondui.ui.modifiers.Magic
import pondui.ui.modifiers.filterKeyPress
import pondui.ui.modifiers.ifTrue
import pondui.ui.modifiers.onEnterPressed
import pondui.ui.modifiers.selected
import pondui.ui.theme.Pond

@Composable
fun EditText(
    text: String,
    placeholder: String,
    style: TextStyle = Pond.typo.body,
    isEditable: Boolean = true,
    isContainerVisible: Boolean = false,
    initialSelectAll: Boolean = true,
    color: Color = Pond.localColors.content,
    padding: PaddingValues = Pond.ruler.unitPadding,
    background: Color = Pond.localColors.section,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    modifier: Modifier = Modifier,
    onAcceptEdit: (String) -> Unit,
) {
    var isEditing by remember { mutableStateOf(false) }
    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = text.takeIf { it.isNotEmpty() } ?: placeholder,
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
        fieldValue = fieldValue.copy(text = text)
        isEditing = false
    }

    Box(
        modifier = modifier
            .clip(Pond.ruler.unitCorners)
            .ifTrue(!isEditing && isEditable) {
                clickable { isEditing = true }
            }
            .ifTrue(isContainerVisible) {
                background(background)
                    .selected(isEditing, padding = 0.dp, radius = Pond.ruler.unitCorner)
                    .padding(padding)
            }
    ) {
        val focusRequester = remember { FocusRequester() }

        if (isEditing) {
            LaunchedEffect(Unit) { focusRequester.requestFocus() }

            BasicTextField(
                value = fieldValue,
                onValueChange = { fieldValue = it },
                modifier = Modifier.width(IntrinsicSize.Min)
                    .focusRequester(focusRequester)
                    .filterKeyPress(Key.Tab)
                    .onEnterPressed { acceptEdit(fieldValue.text) },
                textStyle = style.copy(color = color),
                maxLines = maxLines,
                cursorBrush = SolidColor(color)
            )
        } else {
            val textColor = if (fieldValue.text.isNotEmpty()) color else Pond.localColors.contentDim
            Text(
                text = fieldValue.text.takeIf { it.isNotEmpty() } ?: placeholder,
                style = style,
                color = textColor,
                maxLines = maxLines,
                overflow = overflow,
                modifier = Modifier
            )
        }

        val offset = 36.dp

        Magic(
            isVisible = isEditing,
            offsetX = (-20).dp,
            modifier = Modifier.align(Alignment.BottomEnd)
                .offset(y = offset)
        ) {
            Popup(
                alignment = Alignment.BottomEnd,
            ) {
                ControlSet {
                    Button(
                        TablerIcons.X,
                        isEnabled = isEditing,
                        color = Pond.colors.secondary,
                    ) { cancelEdit(text) }
                    Button(
                        TablerIcons.Check,
                        isEnabled = isEditing,
                    ) { acceptEdit(fieldValue.text) }
                }
            }
        }
    }
}