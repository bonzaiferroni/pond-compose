package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import pondui.ui.theme.Pond

@Composable
fun SettingDrawer(
    value: String,
    label: String,
    shape: Shape = Pond.ruler.pillTopRoundedBottom,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.clip(shape)
            .animateContentSize()
    ) {
        Row(1) {
            Label("$label:")
            Text(value)
            Expando()
            Button(if (isOpen) "Close" else "Change") { isOpen = !isOpen }
        }

    }
}