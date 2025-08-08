package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import kotlinx.collections.immutable.toImmutableList

@Composable
fun IntegerWheel(
    value: Int,
    minValue: Int = 0,
    maxValue: Int = 10,
    label: String? = null,
    onValueChanged: (Int) -> Unit
) {
    val options = remember(minValue, maxValue) { (minValue..maxValue).toImmutableList() }
    MenuWheel(
        selectedItem = value,
        options = options,
        itemAlignment = Alignment.End,
        onSelect = onValueChanged,
        label = label
    )
}