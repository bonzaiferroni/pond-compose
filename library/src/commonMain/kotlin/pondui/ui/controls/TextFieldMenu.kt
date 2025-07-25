package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import kotlinx.collections.immutable.ImmutableList
import pondui.ui.behavior.MagicItem
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
    items: ImmutableList<T>,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    maxSuggestions: Int = 5,
    onTextChanged: (String) -> Unit,
    onChooseSuggestion: (T) -> Unit,
    onEnterPressed: () -> Unit,
    suggestionContent: @Composable (T) -> Unit,
) {
    var isOpen by remember { mutableStateOf(false) }
    var menuSize by remember { mutableStateOf(DpSize.Zero) }
    var selectionIndex by remember { mutableStateOf<Int?>(null) }
    val density = LocalDensity.current
    LaunchedEffect(items) {
        isOpen = items.isNotEmpty()
        selectionIndex = null
    }

    val suggestionCount = minOf(items.size, maxSuggestions)

    Box(
        modifier = modifier
            .onGloballyPositioned { menuSize = it.size.toDpSize(density) }
            .onHotKey(Key.DirectionDown) {
                if (items.isEmpty()) return@onHotKey
                selectionIndex = ((selectionIndex ?: -1) + 1) % suggestionCount
            }
            .onHotKey(Key.DirectionUp) {
                if (items.isEmpty()) return@onHotKey
                selectionIndex = ((selectionIndex ?: 0) + suggestionCount - 1) % suggestionCount
            },
        contentAlignment = Alignment.BottomStart
    ) {
        TextField(
            text = text,
            onTextChanged = onTextChanged,
            maxLines = 1,
            modifier = Modifier.onEnterPressed {
                val index = selectionIndex
                if (index != null && index < items.size) {
                    onChooseSuggestion(items[index])
                } else {
                    onEnterPressed()
                }
            },
            placeholder = placeholder,
            label = label,
        )
        Popup(
            popupPositionProvider = positionProvider,
            onDismissRequest = { if (isOpen) isOpen = false },
        ) {
            MagicItem(
                item = items,
                scale = .8f,
            ) { suggestions ->
                Column(
                    spacingUnits = 1,
                    modifier = Modifier.padTop(1)
                        .width(menuSize.width)
                        .clip(Pond.ruler.unitCorners)
                        .background(Pond.colors.void)
                ) {
                    suggestions.forEachIndexed { index, suggestion ->
                        if (index >= maxSuggestions) return@Column
                        val background = when {
                            index == selectionIndex -> Pond.colors.selected.mixWith(Pond.colors.void)
                            else -> Pond.colors.void
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .magicBackground(background)
                                .actionable { onChooseSuggestion(suggestion) }
                                .magic(scale = .8f, delay = index * 100)
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