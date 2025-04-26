package pondui.ui.controls

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pondui.ui.theme.Pond

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Controls(
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
    content: @Composable FlowRowScope.() -> Unit,
) = FlowRow (
    horizontalArrangement = Pond.ruler.rowTight,
    verticalArrangement = Pond.ruler.columnTight,
    maxItemsInEachRow = maxItemsInEachRow,
    modifier = modifier.clip(Pond.ruler.rounded)
) {
    content()
}