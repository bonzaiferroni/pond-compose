package pondui.ui.controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pondui.ui.theme.Pond

@Composable
fun ControlColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) = Column(
    verticalArrangement = Pond.ruler.columnUnit,
    modifier = modifier.clip(Pond.ruler.rounded)
) {
    content()
}