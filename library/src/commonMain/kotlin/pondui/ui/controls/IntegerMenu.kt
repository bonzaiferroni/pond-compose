package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.collections.immutable.toImmutableList

@Composable
fun IntegerMenu(
    value: Int,
    minValue: Int = 0,
    maxValue: Int = 10,
    onValueChanged: (Int) -> Unit
) {
    val options = remember(minValue, maxValue) { (minValue..maxValue).map{ it.toString() }.toImmutableList() }
    DropMenu(value.toString(), options) {
        onValueChanged(it.toInt())
    }
}