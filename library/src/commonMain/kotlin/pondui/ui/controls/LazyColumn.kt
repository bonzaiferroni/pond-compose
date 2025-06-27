package pondui.ui.controls

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
    state: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Pond.ruler.columnArrangement(spacingUnits),
        state = state,
        modifier = modifier,
        content = content
    )
}

@Composable
fun LazyRow(
    spacingUnits: Int,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    state: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Pond.ruler.rowArrangement(spacingUnits),
        state = state,
        modifier = modifier,
        content = content
    )
}