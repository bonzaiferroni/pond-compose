package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronLeft
import compose.icons.tablericons.ChevronRight
import pondui.ui.core.StateScope
import pondui.ui.modifiers.ifNotNull
import kotlin.math.absoluteValue

@Composable
fun Carousel(
    content: CarouselScope.() -> Unit
) {
    val scope = remember { CarouselScope() }
    scope.content()

    val state by scope.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { state.items.size })
    val density = LocalDensity.current

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
            println("$page: ${pagerState.currentPageOffsetFraction}")
            alpha = animation
            scaleY = animation
            scaleX = animation
            val rotation = 180 * (1 - animation) * -direction
            translationX = -rotation
            rotationY = rotation
        }) {
            Row(1) {
                if (page > 0) {
                    val icon = state.items[page - 1].icon ?: TablerIcons.ChevronLeft
                    IconButton(icon) {
                        scope.changePage(page - 1)
                    }
                }
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    state.items[page].content()
                }
                if (page < pagerState.pageCount - 1) {
                    val icon = state.items[page + 1].icon ?: TablerIcons.ChevronRight
                    IconButton(icon) {
                        scope.changePage(page + 1)
                    }
                }
            }
        }
    }
}

class CarouselScope : StateScope<CarouselState>(CarouselState()) {
    fun addItem(
        icon: ImageVector? = null,
        key: String = stateNow.items.size.toString(),
        isVisible: Boolean = true,
        content: @Composable () -> Unit
    ) {
        if (stateNow.items.any { it.key == key }) return
        val item = CarouselItem(key, isVisible, icon, content)
        val currentLabel = stateNow.currentKey.takeIf { it.isNotEmpty() } ?: item.key
        setState { it.copy(items = it.items + item, currentKey = currentLabel) }
    }

    fun changeVisibility(label: String, isVisible: Boolean) {
        if (stateNow.items.all { it.key != label || it.isVisible == isVisible }) return
        val items = stateNow.items.map { if (it.key == label) it.copy(isVisible = isVisible) else it }
        setState { it.copy(items = items) }
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
    val items: List<CarouselItem> = emptyList(),
)

data class CarouselItem(
    val key: String,
    val isVisible: Boolean,
    val icon: ImageVector?,
    val content: @Composable () -> Unit,
)