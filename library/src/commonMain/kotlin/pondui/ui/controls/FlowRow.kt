package pondui.ui.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    gap: Int,
    modifier: Modifier = Modifier,
    verticalGap: Int = gap,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    itemVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Pond.ruler.rowArrangement(gap),
    verticalArrangement: Arrangement.Vertical = Pond.ruler.columnArrangement(verticalGap),
    content: @Composable FlowRowScope.() -> Unit
) {
    FlowRow(
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        itemVerticalAlignment = itemVerticalAlignment,
        maxItemsInEachRow = maxItemsInEachRow,
        modifier = modifier
    ) {
        content()
    }
}