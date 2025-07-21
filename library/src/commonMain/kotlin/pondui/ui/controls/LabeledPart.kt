package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.TablerIcons
import compose.icons.tablericons.DotsVertical
import pondui.ui.nav.ContextMenu
import pondui.ui.theme.Pond

@Composable
fun HeaderLabel(
    label: String,
    modifier: Modifier = Modifier,
    background: Color = Pond.colors.void,
    shape: Shape = Pond.ruler.shroomed,
    contextMenuContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier.clip(shape)
            .background(background.copy(.5f))
            .padding(vertical = Pond.ruler.unitSpacing, horizontal = Pond.ruler.doubleSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Label(
            text = label,
            style = Pond.typo.h4.copy(letterSpacing = 1.5.sp),
            modifier = Modifier.weight(1f)
        )
        if (contextMenuContent != null) {
            var isMenuVisible by remember { mutableStateOf(false) }
            IconButton(TablerIcons.DotsVertical, modifier = Modifier.size(30.dp)) { isMenuVisible = !isMenuVisible }
            ContextMenu(isMenuVisible, { isMenuVisible = false }) {
                contextMenuContent()
            }
        }
    }
}

fun Modifier.matchParentWidth() = this.then(object : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        // take the parent’s assigned maxWidth…
        val targetWidth = constraints.maxWidth
        // measure child with that exact width
        val placeable = measurable.measure(
            constraints.copy(minWidth = targetWidth, maxWidth = targetWidth)
        )
        return layout(targetWidth, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
})