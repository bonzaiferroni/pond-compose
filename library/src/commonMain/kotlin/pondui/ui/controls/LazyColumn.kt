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

@Composable
fun LazyColumn(
    gap: Int,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    state: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Pond.ruler.columnArrangement(gap),
        state = state,
        modifier = modifier,
        content = content
    )
}

@Composable
fun LazyRow(
    gap: Int,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    state: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Pond.ruler.rowArrangement(gap),
        state = state,
        modifier = modifier,
        content = content
    )
}