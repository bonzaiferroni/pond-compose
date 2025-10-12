package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LabeledValue(
    label: String,
    value: Any?,
    modifier: Modifier = Modifier,
    labelPosition: LabelPosition = LabelPosition.Left,
    gap: Int = if (labelPosition == LabelPosition.Top) 0 else 1
) {
    LabeledContent(
        label,
        modifier,
        labelPosition,
        gap
    ) {
        Text(value.toString())
    }
}