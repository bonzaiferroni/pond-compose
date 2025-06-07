package pondui.ui.controls

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond
import pondui.ui.theme.Spacing
import pondui.ui.theme.toColumnArrangement
import pondui.ui.theme.toRowArrangement

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    unitSpacing: Int,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    itemVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    modifier: Modifier = Modifier,
    content: @Composable FlowRowScope.() -> Unit
) {
    FlowRow(
        verticalArrangement = Pond.ruler.columnArrangement(unitSpacing),
        horizontalArrangement = Pond.ruler.rowArrangement(unitSpacing),
        itemVerticalAlignment = itemVerticalAlignment,
        maxItemsInEachRow = maxItemsInEachRow,
        modifier = modifier
    ) {
        content()
    }
}