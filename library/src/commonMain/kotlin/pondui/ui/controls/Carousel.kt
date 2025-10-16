package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.util.lerp
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronLeft
import compose.icons.tablericons.ChevronRight
import pondui.ui.core.StateScope
import kotlin.math.absoluteValue

@Composable
fun Carousel(
    content: CarouselScope.() -> Unit
) {
    val scope = remember { CarouselScope() }
    scope.reset()
    scope.content()
    val items = scope.items

    val state by scope.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { items.size })

    LaunchedEffect(state.pageIndex) {
        pagerState.animateScrollToPage(state.pageIndex, animationSpec = tween(500))
    }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.animateContentSize(),
        verticalAlignment = Alignment.Top
    ) { page ->
        Box(modifier = Modifier.graphicsLayer {
            val offset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
            val offsetAbs = offset.absoluteValue
            val direction = offset / (offsetAbs.takeIf { it > 0 } ?: 1f)

            val animation = lerp(
                start = 0f,
                stop = 1f,
                fraction = 1f - offsetAbs.coerceIn(0f, 1f)
            )
            alpha = animation
            scaleY = animation
            scaleX = animation
            val rotation = 180 * (1 - animation) * -direction
            translationX = -rotation
            rotationY = rotation
        }) {
            Row(1) {
                if (page > 0) {
                    val icon = items[page - 1].icon ?: TablerIcons.ChevronLeft
                    IconButton(icon) {
                        scope.changePage(page - 1)
                    }
                }
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    items[page].content()
                }
                if (page < pagerState.pageCount - 1) {
                    val icon = items[page + 1].icon ?: TablerIcons.ChevronRight
                    IconButton(icon) {
                        scope.changePage(page + 1)
                    }
                }
            }
        }
    }
}

class CarouselScope : StateScope<CarouselState>(CarouselState()) {
    private val _items: MutableList<CarouselItem> = mutableListOf()
    val items: List<CarouselItem> = _items

    fun reset() = _items.clear()

    fun addItem(
        key: String,
        icon: ImageVector? = null,
        isVisible: Boolean = true,
        content: @Composable () -> Unit
    ) {
        val item = CarouselItem(key, isVisible, icon, content)
        _items.add(item)
        if (stateNow.currentKey.isEmpty()) {
            setState { it.copy(currentKey = key) }
        }
    }

    fun changePage(pageIndex: Int) {
        setState {
            it.copy(
                pageIndex = pageIndex
            )
        }
    }

//    fun changeItem(tabName: String) {
//        val currentLabel = stateNow.currentKey
//        val items = stateNow.items
//        val startIndex = items.indexOfFirst { it.key == currentLabel }.takeIf { it != -1 } ?: return
//        val item = items.firstOrNull() { it.label == tabName } ?: items.first { it.isVisible }
//        if (item.label == currentLabel) return
//        val endIndex = items.indexOfFirst { it.label == tabName }
//        setState { it.copy(indexDelta = endIndex - startIndex, currentKey = item.label) }
//    }
}

data class CarouselState(
    val currentKey: String = "",
    val pageIndex: Int = 0,
)

data class CarouselItem(
    val key: String,
    val isVisible: Boolean,
    val icon: ImageVector?,
    val content: @Composable () -> Unit,
)