package pondui.ui.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import kotlinx.coroutines.delay
import pondui.ui.behavior.Magic
import pondui.ui.behavior.magic
import pondui.ui.behavior.magicBackground
import pondui.ui.behavior.onEnterPressed
import pondui.ui.behavior.onHotKey
import pondui.ui.behavior.padTop
import pondui.ui.theme.Pond
import pondui.utils.mixWith

@Composable
fun <T> TextFieldMenu(
    text: String,
    provideOptions: () -> List<T>,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    maxSuggestions: Int = 5,
    minWidth: Dp = 150.dp,
    onTextChanged: (String) -> Unit,
    onChooseSuggestion: (T) -> Unit,
    onEnterPressed: () -> Unit,
    suggestionContent: @Composable (T) -> Unit,
) {
    var isOpen by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var menuSize by remember { mutableStateOf(DpSize.Zero) }
    var selectionIndex by remember { mutableStateOf<Int?>(null) }
    val density = LocalDensity.current
    LaunchedEffect(provideOptions) {
        // isOpen = items.isNotEmpty()
        selectionIndex = null
    }
    var items by remember { mutableStateOf<List<T>>(emptyList())}

    LaunchedEffect(text, isFocused) {
        if (isFocused) {
            println("fetching options")
            items = provideOptions()
            println(items.size)
            isOpen = items.isNotEmpty()
        }
    }

    val suggestionCount = remember(items) { minOf(items.size, maxSuggestions) }

    Box(
        contentAlignment = Alignment.BottomStart,
        modifier = modifier.width(IntrinsicSize.Max)
            .onGloballyPositioned { menuSize = it.size.toDpSize(density) }
            .onHotKey(Key.DirectionDown) {
                if (items.isEmpty()) return@onHotKey
                selectionIndex = ((selectionIndex ?: -1) + 1) % suggestionCount
            }
            .onHotKey(Key.DirectionUp) {
                if (items.isEmpty()) return@onHotKey
                selectionIndex = ((selectionIndex ?: 0) + suggestionCount - 1) % suggestionCount
            }
            .onHotKey(Key.Escape) { isOpen = false }
    ) {
        TextField(
            text = text,
            onTextChanged = {
                isOpen = items.isNotEmpty()
                onTextChanged(it)
            },
            maxLines = 1,
            minWidth = minWidth,
            placeholder = placeholder,
            label = label,
            onFocusChanged = { isFocused = it },
            modifier = Modifier.fillMaxWidth()
                .onEnterPressed {
                    val index = selectionIndex
                    if (index != null && index < items.size) {
                        onChooseSuggestion(items[index])
                    } else {
                        onEnterPressed()
                    }
                }
        )
        Popup(
            popupPositionProvider = positionProvider,
            onDismissRequest = { if (isOpen) isOpen = false },
        ) {
            Magic(isOpen, scale = .8f) {
                Column(
                    modifier = Modifier.padTop(1)
                        .width(menuSize.width)
                        .clip(Pond.ruler.unitCorners)
                ) {
                    items.forEachIndexed { index, suggestion ->
                        if (index >= maxSuggestions) return@Column
                        val background = when {
                            index == selectionIndex -> Pond.colors.selection.mixWith(Pond.colors.void)
                            else -> Pond.colors.selectionVoid
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .magic(offsetY = (-20).dp, delay = index * 100)
                                .magicBackground(background)
                                .actionable { onChooseSuggestion(suggestion) }
                                .padding(vertical = Pond.ruler.unitSpacing, horizontal = Pond.ruler.doubleSpacing)
                        ) {
                            suggestionContent(suggestion)
                        }
                    }
                }
            }
        }
    }
}

private val positionProvider = object : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ) = IntOffset(anchorBounds.left, anchorBounds.bottom)
}

fun IntSize.toDpSize(density: Density): DpSize = with(density) { DpSize(width.toDp(), height.toDp()) }