package pondui.ui.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pondui.ui.theme.Spacing

@Composable
fun Column(
    spacing: Spacing,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = when (spacing) {
            Spacing.Tight -> pondui.ui.theme.Pond.ruler.columnTight
            Spacing.Grouped -> pondui.ui.theme.Pond.ruler.columnGrouped
            Spacing.Spaced -> pondui.ui.theme.Pond.ruler.columnSpaced
        },
        modifier = modifier,
        content = content
    )
}