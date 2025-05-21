package pondui.ui.controls

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pondui.ui.theme.Spacing
import pondui.ui.theme.toColumnArrangement
import pondui.ui.theme.toRowArrangement

@Composable
fun FlowRow(
    spacing: Spacing,
    modifier: Modifier = Modifier,
    content: @Composable FlowRowScope.() -> Unit
) {
    FlowRow(
        verticalArrangement = spacing.toColumnArrangement(),
        horizontalArrangement = spacing.toRowArrangement(),
        modifier = modifier
    ) {
        content()
    }
}