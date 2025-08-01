package pondui.ui.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pondui.ui.controls.Column
import pondui.ui.controls.H3
import pondui.ui.theme.Pond

@Composable
fun ChartBox(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        gap = 1,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clip(Pond.ruler.defaultCorners)
            .background(Pond.colors.void.copy(.5f))
            .padding(Pond.ruler.unitPadding)
    ) {
        H3(title)
        content()
    }
}