package pondui.ui.controls

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pondui.ui.theme.Pond

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Controls(
    modifier: Modifier = Modifier,
    content: @Composable FlowRowScope.() -> Unit,
) = FlowRow (
    horizontalArrangement = Pond.ruler.rowTight,
    verticalArrangement = Pond.ruler.columnTight,
    modifier = modifier.clip(Pond.ruler.rounded)
) {
    content()
}