package pondui.ui.controls

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import pondui.ui.modifiers.ifTrue
import pondui.ui.theme.Pond

@Composable
fun <T> ReorderableColumn(
    items: List<T>,
    modifier: Modifier = Modifier,
    isReorderable: Boolean = true,
    gap: Int = 1,
    onChange: (List<T>) -> Unit,
    provideContent: @Composable (Int, T) -> Unit
) {
    if (items.isEmpty()) return
    val latestItems by rememberUpdatedState(items)
    var draggingIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffsetY by remember { mutableStateOf(0f) }
    val targetIndexState = remember { mutableStateOf<Int?>(null) }
    val density = LocalDensity.current
    val unitDp = Pond.ruler.unitSpacing
    val gapPx = with(density) { (unitDp * gap).roundToPx() }

    fun finishDrag() {
        val fromIndex = draggingIndex
        val toIndex = targetIndexState.value
        if (fromIndex != null && toIndex != null && fromIndex != toIndex) {
            val newList = latestItems.toMutableList().apply {
                add(toIndex, removeAt(fromIndex))
            }
            onChange(newList)
        }
        draggingIndex = null
        dragOffsetY = 0f
        targetIndexState.value = null
    }

    Layout(
        content = {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .then(if (draggingIndex == index) Modifier.zIndex(1f) else Modifier)
                        .ifTrue(isReorderable) {
                            pointerInput(index) {
                                detectDragGestures(
                                    onDragStart = { draggingIndex = index },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        dragOffsetY += dragAmount.y
                                    },
                                    onDragEnd = { finishDrag() },
                                    onDragCancel = { finishDrag() }
                                )
                            }
                        }
                ) {
                    provideContent(index, item)
                }
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val heights = placeables.map { it.height }
        val baseY = IntArray(placeables.size)
        var currentY = 0
        for (i in heights.indices) {
            baseY[i] = currentY
            currentY += heights[i] + gapPx
        }

        fun centerAt(i: Int): Float = baseY[i] + heights[i] / 2f

        draggingIndex?.let { originIndex ->
            val draggedCenter = baseY[originIndex] + dragOffsetY + heights[originIndex] / 2f
            var newIndex = originIndex

            // Move down: cross midpoints between (k, k+1)
            while (newIndex < placeables.lastIndex) {
                val boundary = (centerAt(newIndex) + centerAt(newIndex + 1)) / 2f
                if (draggedCenter > boundary) newIndex++ else break
            }
            // Move up: cross midpoints between (k-1, k)
            while (newIndex > 0) {
                val boundary = (centerAt(newIndex - 1) + centerAt(newIndex)) / 2f
                if (draggedCenter < boundary) newIndex-- else break
            }

            targetIndexState.value = newIndex.coerceIn(0, items.lastIndex)
        }

        val maxWidth = placeables.maxOfOrNull { it.width } ?: 0
        layout(maxWidth, currentY - gapPx) {
            for (i in placeables.indices) {
                val placeable = placeables[i]
                if (i == draggingIndex) {
                    placeable.placeRelative(0, (baseY[i] + dragOffsetY).toInt())
                } else {
                    var y = baseY[i]
                    draggingIndex?.let { origin ->
                        val target = targetIndexState.value ?: origin
                        if (target > origin && i in (origin + 1)..target) {
                            y -= heights[origin] + gapPx
                        } else if (target < origin && i in target until origin) {
                            y += heights[origin] + gapPx
                        }
                    }
                    placeable.placeRelative(0, y)
                }
            }
        }
    }
}