package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import compose.icons.tablericons.X
import pondui.ui.behavior.Magic
import pondui.ui.behavior.magic
import pondui.ui.behavior.onEnterPressed
import pondui.ui.theme.Pond

@Composable
fun EditText(
    text: String,
    onTextChange: (String) -> Unit,
    onAcceptEdit: (String) -> Unit,
    color: Color = Pond.localColors.content,
    style: TextStyle = Pond.typo.body,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var cachedText by remember { mutableStateOf(text) }

    LaunchedEffect(isEditing) {
        cachedText = text
    }

    fun acceptEdit(value: String) {
        onAcceptEdit(value)
        isEditing = false
    }

    fun cancelEdit(value: String) {
        onTextChange(value)
        isEditing = false
    }

    Box(
        modifier = Modifier.clickable(enabled = !isEditing) { isEditing = true }
    ) {
        Magic(!isEditing) {
            Text(text, color, style, maxLines, overflow, modifier)
        }
        Magic(isEditing) {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = modifier.width(IntrinsicSize.Min).onEnterPressed { acceptEdit(text) },
                textStyle = style.copy(color = color),
                maxLines = maxLines
            )
        }
        val offset = with (LocalDensity.current) { 32.dp.toPx().toInt() }
        Popup(
            alignment = Alignment.CenterStart,
            offset = IntOffset(-offset, 0)
        ) {
            Button(
                TablerIcons.X,
                background = Pond.colors.tertiary,
                modifier = Modifier
                    .magic(isEditing, offsetX = offset)
            ) { cancelEdit(cachedText) }
        }

        Popup(
            alignment = Alignment.CenterEnd,
            offset = IntOffset(offset, 0)
        ) {
            Button(
                TablerIcons.Check,
                modifier = Modifier
                    .magic(isEditing, offsetX = -offset)
            ) { acceptEdit(text) }
        }
    }
}