package pondui.ui.controls

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import pondui.ui.theme.Pond
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun <T> RoloMenu(
    selectedItem: T,
    options: ImmutableList<T>,
    label: String? = null,
    toLabel: (T) -> String = { it.toString() },
    offsetRowCount: Int = 1,
    rowHeight: Dp = 18.dp,
    indicatorColor: Color = Pond.colors.primary,
    modifier: Modifier = Modifier,
    onSelect: (T) -> Unit
) {
    val listState = rememberLazyListState(options.indexOf(selectedItem))
    val flingBehavior = rememberSnapFlingBehavior(listState)
    val scope = rememberCoroutineScope()
    val isScrollInProgress = listState.isScrollInProgress
    var isScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(isScrollInProgress) {
        isScrolling = isScrollInProgress
        if (!isScrollInProgress) {
            val index = calculateSnappedItemIndex(listState)
            onSelect(options[index])
        }
    }

    val rowCount = offsetRowCount * 2 + 1
    val wheelHeight = rowHeight * rowCount
    val color = Pond.localColors.content
    val layoutInfo = listState.layoutInfo
    val viewPortHeight = layoutInfo.viewportSize.height.toFloat()
    val rowHeightPx = viewPortHeight / rowCount
    val centerIndex = listState.firstVisibleItemIndex
    val scrollOffset = listState.firstVisibleItemScrollOffset
    val isScrollingAnimation by animateFloatAsState(if (isScrolling) 1f else 0f)

    Row(
        spacingUnits = 1,
        modifier = modifier
            .drawBehind {
                val width = size.width * (1 - isScrollingAnimation)
                drawRect(
                    color = indicatorColor,
                    topLeft = Offset(size.width / 2 * isScrollingAnimation, rowHeightPx * 2 - 2),
                    size = Size(width, 2f)
                )
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = {
                        isScrolling = true
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()  // mark it handled
                        scope.launch {
                            listState.scrollBy(-dragAmount)
                        }
                    },
                    onDragEnd = {
                        isScrolling = false
                        // ⚓️ call yer function here
                        val index = calculateSnappedItemIndex(listState)
                        // println("scrolling to $index")
                        scope.launch {
                            listState.scrollToItem(index)
                            onSelect(options[index])
                        }
                    }
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = rowHeight * offsetRowCount),
            modifier = modifier.height(wheelHeight)
                .animateContentSize(),
            horizontalAlignment = Alignment.End
        ) {
            itemsIndexed(options) { index, option ->
                val label = toLabel(option)
                val indexCenterDelta = index - centerIndex
                val rowCenterDelta = indexCenterDelta * rowHeightPx.toInt() - scrollOffset
                val absOffset = abs(rowCenterDelta)
                val alpha = if (absOffset >= 0 && absOffset <= rowHeightPx.toInt()) {
                    1.2f - (absOffset / rowHeightPx)
                } else {
                    0.2f
                }

                val maxTiltDeg = 40f
                val rotationX = (-maxTiltDeg * (rowCenterDelta / rowHeightPx)).takeUnless { it.isNaN() } ?: 0f

                val radiusPx = ((rowHeightPx * rowCount) / 2)
                val angleRad = maxTiltDeg * (rowCenterDelta / rowHeightPx) * (PI.toFloat() / 180f)
                val wheelY = radiusPx * sin(angleRad)
                val translationY = wheelY - rowCenterDelta

                BasicText(
                    text = label,
                    color = { color },
                    modifier = Modifier
                        .height(rowHeight)
                        .graphicsLayer {
                            this.rotationX = rotationX
                            this.alpha = alpha
                            this.translationY = translationY
                        },
                )
            }
        }
        label?.let { Label(it) }
    }
}

private fun calculateSnappedItemIndex(lazyListState: LazyListState): Int {
    val currentItemIndex = lazyListState.firstVisibleItemIndex
    val itemCount = lazyListState.layoutInfo.totalItemsCount
    val offset = lazyListState.firstVisibleItemScrollOffset
    val itemHeight = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: return currentItemIndex

    return if (offset > itemHeight / 2 && currentItemIndex < itemCount - 1) {
        currentItemIndex + 1
    } else {
        currentItemIndex
    }
}