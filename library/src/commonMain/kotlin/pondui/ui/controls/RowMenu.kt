package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronLeft
import compose.icons.tablericons.Dots
import kotlinx.collections.immutable.ImmutableList
import pondui.ui.behavior.Magic
import pondui.ui.theme.Pond

@Composable
fun RowMenu(
    closedIcon: ImageVector = TablerIcons.Dots,
    openIcon: ImageVector = TablerIcons.ChevronLeft,
    items: ImmutableList<RowMenuItem>,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(1, modifier = modifier) {
        Magic(isExpanded, offsetX = 20) {
            ControlSet {
                for (item in items) {
                    Button(item.icon, onClick = item.action, background = item.background ?: Pond.colors.secondary)
                }
            }
        }
        Button(
            background = if (isExpanded) Pond.colors.primary else Pond.colors.secondary,
            padding = Pond.ruler.unitPadding,
            onClick = { isExpanded = !isExpanded },
            shape = Pond.ruler.pill
        ) {
            Magic(!isExpanded, offsetX = -5) {
                Icon(closedIcon)
            }
            Magic(isExpanded, offsetX = 5) {
                Icon(openIcon)
            }
        }
    }
}

data class RowMenuItem(
    val icon: ImageVector,
    val background: Color? = null,
    val action: () -> Unit
)