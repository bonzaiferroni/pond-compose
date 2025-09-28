package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LabeledValue(
    label: String,
    value: Any?,
    modifier: Modifier = Modifier,
    labelPosition: LabelPosition = LabelPosition.Top
) {
    when (labelPosition) {
        LabelPosition.Top -> Column(0, modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Label(label)
            Text(value.toString())
        }
        LabelPosition.Right -> Row(1, modifier = modifier) {
            Text(value.toString())
            Label(label)
        }
        LabelPosition.Bottom -> Column(0, modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value.toString())
            Label(label)
        }
        LabelPosition.Left -> Row(1, modifier = modifier) {
            Label(label)
            Text(value.toString())
        }
    }
}

enum class LabelPosition {
    Top,
    Right,
    Bottom,
    Left
}