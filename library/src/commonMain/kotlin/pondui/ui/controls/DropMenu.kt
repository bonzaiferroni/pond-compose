package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import pondui.ui.behavior.AlignX
import pondui.ui.behavior.Magic
import pondui.ui.behavior.drawLabel
import pondui.ui.behavior.ifNotNull
import pondui.ui.behavior.magic
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors
import pondui.utils.glowWith
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
    val background = Pond.colors.void.mixWith(color)
    val density = LocalDensity.current

    ProvideSkyColors {
        Row(
            modifier = modifier.magic(!isOpen)
                .clip(Pond.ruler.pill)
                .drawBehind {
                    drawRoundRect(
                        color = background,
                    )
                }
                .onGloballyPositioned { menuSize = it.size }
                .padding(Pond.ruler.unitPadding)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            label?.let {
                Label("$label:", modifier = Modifier.padding(horizontal = Pond.ruler.unitSpacing))
            }
            Text(
                text = selected,
                modifier = Modifier.padding(horizontal = Pond.ruler.unitSpacing)
            )
            Button(
                imageVector = TablerIcons.ChevronDown,
                background = color,
                isEnabled = !isOpen,
                padding = Pond.ruler.halfPadding,
            ) {
                isOpen = !isOpen
            }
        }
        Popup(
            alignment = Alignment.TopStart,
            onDismissRequest = { if (isOpen) isOpen = false },
        ) {
            Magic(
                isVisible = isOpen,
                scale = .8f,
                offsetY = (-20).dp
            ) {
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max)
                        .widthIn(min = with(density) { menuSize.width.toDp() })
                        .ifNotNull(label) { drawLabel(it, alignX = AlignX.Center) }
                        .clip(Pond.ruler.defaultCorners)
                        .background(background)
                ) {
                    for (option in options) {
                        TextButton(
                            text = option,
                            style = Pond.typo.body.copy(textAlign = TextAlign.Center),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            onSelect(option)
                            isOpen = false
                        }
                    }
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
inline fun <reified T> DropMenu(
    selected: T,
    modifier: Modifier = Modifier,
    color: Color = Pond.colors.primary,
    label: String? = null,
    crossinline onSelect: (T) -> Unit
) where T : Enum<T>, T : LabeledEnum<T> {
    val enums = remember {
        val typeName = T::class.nameOrError
        (enumArrays[typeName] ?: enumValues<T>().also { enumArrays[typeName] = it }) as Array<T>
    }
    val values = remember {
        val typeName = T::class.nameOrError
        enumLabels[typeName] ?: enums.map { it.label }.toImmutableList().also { enumLabels[typeName] = it }
    }

    DropMenu(
        selected = selected.label,
        options = values,
        modifier = modifier,
        color = color,
        label = label
    ) { stringValue ->
        val value = enums.firstOrNull { e -> e.label == stringValue } ?: error("enum value not found")
        onSelect(value)
    }
}

val enumArrays: MutableMap<String, Array<*>> = mutableMapOf()
val enumLabels: MutableMap<String, ImmutableList<String>> = mutableMapOf()