package pondui.ui.controls

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pondui.ui.theme.Pond

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ControlSet(
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
    content: @Composable FlowRowScope.() -> Unit,
) = FlowRow (
    horizontalArrangement = Pond.ruler.rowUnit,
    verticalArrangement = Pond.ruler.columnUnit,
    itemVerticalAlignment = Alignment.CenterVertically,
    maxItemsInEachRow = maxItemsInEachRow,
    modifier = modifier.clip(Pond.ruler.midCorners)
) {
    content()
}