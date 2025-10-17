package pondui.ui.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowsSort
import pondui.ui.modifiers.Magic
import pondui.ui.modifiers.size

@Composable
fun <T> Accordion(
    items: List<T>,
    modifier: Modifier = Modifier,
    provideHeader: @Composable (T) -> Unit,
    provideContent: @Composable (Int, T) -> Unit,
) {
    Column(1, modifier = modifier) {
        items.forEachIndexed { index, item ->
            Drawer(
                headerContent = { provideHeader(item) }
            ) { provideContent(index, item) }
        }
    }
}

@Composable
fun <T> ReorderableAccordion(
    items: List<T>,
    modifier: Modifier = Modifier,
    isReorderable: Boolean = true,
    gap: Int = 1,
    onChange: (List<T>) -> Unit,
    provideHeader: @Composable (T) -> Unit,
    provideContent: @Composable (Int, T) -> Unit,
) {
    Column(gap) {
        ReorderableColumn(
            items = items,
            gap = gap,
            isReorderable = isReorderable,
            onChange = onChange,
            modifier = modifier
        ) { index, item ->
            Drawer(
                isEnabled = !isReorderable,
                headerContent = {
                    Row(1) {
                        Box(modifier = Modifier.weight(1f)) {
                            provideHeader(item)
                        }
                        Magic (isReorderable, scale = 1.5f) {
                            Icon(TablerIcons.ArrowsSort, modifier = Modifier.size(3))
                        }

                    }
                }
            ) { provideContent(index, item) }
        }
    }
}