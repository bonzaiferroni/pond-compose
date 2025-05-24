package pondui.ui.controls

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pondui.ui.theme.Spacing
import pondui.ui.theme.toRowArrangement

@Composable
fun Row(
    spacing: Spacing,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) = Row(
    horizontalArrangement = spacing.toRowArrangement(),
    modifier = modifier,
    content = content,
    verticalAlignment = verticalAlignment
)