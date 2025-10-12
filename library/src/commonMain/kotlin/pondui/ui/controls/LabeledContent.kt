package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LabeledContent(
    label: String,
    modifier: Modifier = Modifier,
    labelPosition: LabelPosition = LabelPosition.Left,
    gap: Int = if (labelPosition == LabelPosition.Top) 0 else 1,
    content: @Composable () -> Unit
) {
    when (labelPosition) {
        LabelPosition.Top -> Column(gap, modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Label(label)
            content()
        }
        LabelPosition.Right -> Row(gap, modifier = modifier) {
            content()
            Label(label)
        }
        LabelPosition.Bottom -> Column(gap, modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            content()
            Label(label)
        }
        LabelPosition.Left -> Row(gap, modifier = modifier) {
            Label(label)
            content()
        }
    }
}

enum class LabelPosition {
    Top,
    Right,
    Bottom,
    Left
}