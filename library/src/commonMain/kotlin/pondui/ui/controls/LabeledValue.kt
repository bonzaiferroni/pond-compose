package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LabeledValue(
    label: String,
    value: Any?,
    modifier: Modifier = Modifier,
) {
    Row(1, modifier = modifier) {
        Text(value.toString())
        Label(label)
    }
}