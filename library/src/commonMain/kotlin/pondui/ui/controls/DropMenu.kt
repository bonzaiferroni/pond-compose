package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronDown
import kabinet.model.LabeledEnum
import kabinet.utils.nameOrError
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import pondui.ui.behavior.Magic
import pondui.ui.behavior.magic
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors
import pondui.utils.darken
import pondui.utils.mixWith

@Composable
fun DropMenu(
    selected: String,
    options: ImmutableList<String>,
    onSelect: (String) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }

    ProvideSkyColors {
        Row(
            modifier = Modifier.magic(!isOpen)
                .clip(Pond.ruler.pill)
                .background(Pond.colors.void)
                .padding(Pond.ruler.unitPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected,
                modifier = Modifier.padding(horizontal = Pond.ruler.unitSpacing)
            )
            Button(
                imageVector = TablerIcons.ChevronDown,
                isEnabled = !isOpen,
                padding = Pond.ruler.halfPadding,
            ) {
                isOpen = !isOpen
            }
        }
        Popup(
            onDismissRequest = { if (isOpen) isOpen = false },
        ) {
            Magic(
                isVisible = isOpen,
                scale = .8f,
                offsetY = (-20).dp
            ) {
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max)
                        .clip(Pond.ruler.defaultCorners)
                        .background(Pond.colors.void)
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

    DropMenu(selected.label, values) { stringValue ->
        val value = enums.firstOrNull { e -> e.label == stringValue } ?: error("enum value not found")
        onSelect(value)
    }
}

val enumArrays: MutableMap<String, Array<*>> = mutableMapOf()
val enumLabels: MutableMap<String, ImmutableList<String>> = mutableMapOf()