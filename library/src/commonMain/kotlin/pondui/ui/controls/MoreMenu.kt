package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import compose.icons.TablerIcons
import compose.icons.tablericons.Settings
import pondui.ui.modifiers.Magic
import pondui.ui.modifiers.magic
import pondui.ui.theme.Pond
import pondui.utils.lighten

@Composable
fun MoreMenu(
    icon: ImageVector = TablerIcons.Settings,
    closeIcon: ImageVector = icon,
    content: @Composable MoreMenuScope.() -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    val scope = remember { MoreMenuScope() }
    if (isOpen) {
        scope.reset()
        content(scope)
    }

    Box(
        contentAlignment = Alignment.TopEnd,
    ) {
        val iconColor = if (isOpen) Pond.colors.selection.lighten(.2f) else Pond.localColors.content
        IconButton(icon, isEnabled = !isOpen, tint = iconColor) { isOpen = !isOpen }

        Popup(
            onDismissRequest = { isOpen = false },
            alignment = Alignment.TopEnd,
        ) {
            Magic(isOpen) {
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max)
                        .clip(Pond.ruler.unitCorners)
                        .background(Pond.colors.selectionVoid)
                ) {
                    MoreMenuRow(
                        index = 0,
                        icon = closeIcon,
                        color = Pond.localColors.selectedContent,
                        onClick = { isOpen = false },
                    ) { Text("Close", color = Pond.localColors.contentDim)}

                    scope.items.forEachIndexed { index, item ->
                        MoreMenuRow(
                            index = index + 1,
                            icon = item.icon,
                            color = item.color,
                            onClick = { item.onClick(); isOpen = false },
                            content = item.content
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun MoreMenuRow(
    index: Int,
    icon: ImageVector?,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        gap = 1,
        modifier = modifier.fillMaxWidth()
            .actionable(onClick = onClick)
            .padding(Pond.ruler.unitPadding)
    ) {
        Box(
            modifier = Modifier.weight(1f)
                .magic(offsetX = 20.dp, delay = index * 100)
        ) {
            content()
        }
        icon?.let {
            Icon(
                imageVector = it,
                color = color,
                modifier = Modifier.magic(rotationZ = 180, delay = index * 100)
            )
        }
    }
}

@Composable
fun MoreMenuScope.MoreMenuItem(
    icon: ImageVector? = null,
    color: Color = Pond.localColors.content,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    this.addItem(MoreMenuEntry(icon, color, content, onClick))
}

@Composable
fun MoreMenuScope.MoreMenuItem(
    label: String,
    icon: ImageVector? = null,
    color: Color = Pond.localColors.content,
    onClick: () -> Unit,
) {
    MoreMenuItem(
        icon = icon,
        color = color,
        onClick = onClick,
    ) { Text(label) }
}

class MoreMenuScope {
    private val mutableItems = mutableListOf<MoreMenuEntry>()
    internal val items: List<MoreMenuEntry> = mutableItems

    internal fun reset() {
        mutableItems.clear()
    }

    internal fun addItem(item: MoreMenuEntry) {
        mutableItems.add(item)
    }
}

data class MoreMenuEntry(
    val icon: ImageVector?,
    val color: Color,
    val content: @Composable () -> Unit,
    val onClick: () -> Unit,
)

private val positionProvider = object : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ) = IntOffset(anchorBounds.right, anchorBounds.bottom)
}