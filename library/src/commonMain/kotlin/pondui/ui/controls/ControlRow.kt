package pondui.ui.controls

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pondui.ui.theme.Pond

@Composable
fun ControlRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) = Row(
    horizontalArrangement = Pond.ruler.rowTight,
    modifier = modifier.clip(Pond.ruler.rounded)
) {
    content()
}