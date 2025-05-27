package pondui.ui.controls

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond
import pondui.ui.theme.Spacing
import pondui.ui.theme.toColumnArrangement

@Composable
fun LazyColumn(
    spacingUnits: Int,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Pond.ruler.columnArrangement(spacingUnits),
        modifier = modifier,
        content = content
    )
}