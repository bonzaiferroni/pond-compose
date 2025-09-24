package pondui.ui.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import pondui.ui.theme.ColorMode
import pondui.ui.theme.Pond
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

@Composable
fun <T> MenuWheel(
    selectedItem: T,
    options: ImmutableList<T>,
    modifier: Modifier = Modifier,
    label: String? = null,
    toLabel: (T) -> String = { it.toString() },
    offsetRowCount: Int = 1,
    menuWidth: Dp? = null,
    rowHeight: Dp = 20.dp,
    indicatorColor: Color = Pond.colors.creation,
    itemAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    onSelect: (T) -> Unit
) {
    if (options.isEmpty()) return
    var selectedIndex by remember { mutableStateOf(options.indexOf(selectedItem)) }
    val listState = rememberLazyListState(if (selectedIndex >= 0) selectedIndex else 0)
    val flingBehavior = rememberSnapFlingBehavior(listState)
    val scope = rememberCoroutineScope()
    val isScrollInProgress = listState.isScrollInProgress
    var isScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(isScrollInProgress) {
        isScrolling = isScrollInProgress
        if (!isScrollInProgress) {
            selectedIndex = calculateSnappedItemIndex(listState)
        }
    }

    LaunchedEffect(selectedIndex, options) {
        if (selectedIndex >= 0 && selectedIndex < options.size)
            onSelect(options[selectedIndex])
    }

    val rowCount = offsetRowCount * 2 + 1
    val wheelHeight = rowHeight * rowCount
    val listWidth = remember(menuWidth) { menuWidth ?: (options.maxOf { toLabel(it).length } * 12.dp) }
    val layoutInfo by remember { derivedStateOf { listState.layoutInfo } }
    val viewPortHeight = layoutInfo.viewportSize.height.toFloat()
    val rowHeightPx = viewPortHeight / rowCount
    val centerIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val scrollOffset by remember { derivedStateOf { listState.firstVisibleItemScrollOffset } }
    val isScrollingAnimation by animateFloatAsState(if (isScrolling) 1f else 0f)
    val unitSpacing = Pond.ruler.unitSpacing

    val localColors = Pond.localColors
    val color = localColors.content
    val wheelColor = if (localColors.mode == ColorMode.Sky) Pond.colors.void else Pond.colors.void.copy(.2f)
    val style = Pond.typo.body.copy(fontSize = 16.sp)

    Row(
        gap = 1,
        modifier = modifier
            .drawBehind {
                val stops = arrayOf(
                    0.00f to Color.Transparent,
                    0.30f to wheelColor,
                    0.70f to wheelColor,
                    1.00f to Color.Transparent
                )
                drawRoundRect(
                    brush = Brush.verticalGradient(colorStops = stops),
                    alpha = .6f * isScrollingAnimation + .4f,
                    cornerRadius = CornerRadius(
                        x = unitSpacing.toPx(),
                        y = (wheelHeight / 2).toPx(),
                    ),
                    size = Size(
                        width = size.width + unitSpacing.toPx(),
                        height = size.height
                    ),
                    topLeft = Offset(-(unitSpacing / 2).toPx(), 0f)
                )

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
                            selectedIndex = index
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
            modifier = modifier.width(listWidth)
                .height(wheelHeight),
            horizontalAlignment = itemAlignment
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
                    style = style,
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