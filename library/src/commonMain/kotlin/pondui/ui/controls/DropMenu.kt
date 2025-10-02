package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronDown
import kabinet.model.LabeledEnum
import kabinet.utils.nameOrError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import pondui.ui.modifiers.Magic
import pondui.ui.modifiers.MagicItem
import pondui.ui.modifiers.magic
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors
import pondui.utils.electrify
import pondui.utils.mixWith

@Composable
fun DropMenu(
    selected: String,
    options: ImmutableList<String>,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.primary,
    label: String? = null,
    onSelect: (String) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    var menuSize by remember { mutableStateOf(IntSize.Zero) }
    val background = Pond.colors.void
    val menuBackground = Pond.colors.void.mixWith(color).electrify()
    val density = LocalDensity.current
    val isOpenEnabled = !isOpen && options.isNotEmpty()

    ProvideSkyColors {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = modifier.width(IntrinsicSize.Max)
        ) {
            Row(
                modifier = Modifier.clip(Pond.ruler.roundEnd)
                    .drawBehind { drawRoundRect(background) }
                    .onGloballyPositioned { menuSize = it.size }
                    .actionable(isEnabled = isOpenEnabled, icon = PointerIcon.Hand) { isOpen = !isOpen }
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                label?.let {
                    Label("$label:", modifier = Modifier.padding(horizontal = Pond.ruler.unitSpacing))
                }
                MagicItem(
                    item = selected,
                    offsetX = 20.dp,
                    isVisibleInit = true,
                    modifier = Modifier.weight(1f)
                        .padding(horizontal = Pond.ruler.unitSpacing)
                ) { selected ->
                    Text(
                        text = selected,
                        maxLines = 1
                    )
                }
                Button(
                    imageVector = TablerIcons.ChevronDown,
                    color = color,
                    isEnabled = isOpenEnabled,
                    padding = Pond.ruler.halfPadding,
                ) {
                    isOpen = !isOpen
                }
            }
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, menuSize.height),
                onDismissRequest = { if (isOpen) isOpen = false },
            ) {
                Magic(
                    isVisible = isOpen,
                ) {
                    Column(
                        modifier = Modifier.padding(top = Pond.ruler.unitSpacing)
                            .width(IntrinsicSize.Max)
                            .widthIn(min = with(density) { menuSize.width.toDp() })
                            .clip(Pond.ruler.unitCorners)
                    ) {
                        val selectedIndex = options.indexOfFirst { it == selected }
                        options.forEachIndexed { index, option ->
                            if (index == selectedIndex) return@forEachIndexed
                            val index = if (index > selectedIndex) index - 1 else index
                            DropMenuOption(option, index, color) {
                                onSelect(option)
                                isOpen = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropMenuOption(
    option: String,
    index: Int,
    background: Color,
    onClick: () -> Unit
) {
    Text(
        text = option,
        style = Pond.typo.body.copy(textAlign = TextAlign.Start),
        modifier = Modifier.fillMaxWidth()
            .magic(offsetY = (-30).dp, delay = index * 30)
            .background(background)
            .actionable(onClick = onClick)
            .padding(Pond.ruler.unitPadding),
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
inline fun <reified T> DropMenu(
    selected: T,
    crossinline labelOf: (T) -> String,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.primary,
    label: String? = null,
    crossinline onChange: (T) -> Unit
) where T : Enum<T> {
    val enums = remember {
        val typeName = T::class.nameOrError
        (enumArrays[typeName] ?: enumValues<T>().also { enumArrays[typeName] = it }) as Array<T>
    }
    val values = remember {
        val typeName = T::class.nameOrError
        enumLabels[typeName] ?: enums.map { labelOf(it) }.toImmutableList().also { enumLabels[typeName] = it }
    }

    DropMenu(
        selected = labelOf(selected),
        options = values,
        modifier = modifier,
        color = color,
        label = label
    ) { stringValue ->
        val value = enums.firstOrNull { e -> labelOf(e) == stringValue } ?: error("enum value not found")
        onChange(value)
    }
}

val enumArrays: MutableMap<String, Array<*>> = mutableMapOf()
val enumLabels: MutableMap<String, ImmutableList<String>> = mutableMapOf()