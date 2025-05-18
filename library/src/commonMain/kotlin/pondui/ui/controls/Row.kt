package pondui.ui.controls

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond
import pondui.ui.theme.Spacing

@Composable
fun Row(
    spacing: Spacing,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) = Row(
    horizontalArrangement = when(spacing) {
        Spacing.Tight -> Pond.ruler.rowTight
        Spacing.Grouped -> Pond.ruler.rowGrouped
        Spacing.Spaced -> Pond.ruler.rowSpaced
    },
    modifier = modifier,
    content = content,
    verticalAlignment = verticalAlignment
)