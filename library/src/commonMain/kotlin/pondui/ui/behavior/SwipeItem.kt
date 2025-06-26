package pondui.ui.behavior

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

@Composable
fun <T> SwipeItem(
    item: T,
    fromDirection: FromDirection,
    shape: Shape = RectangleShape,
    onEndSwipe: (T) -> Unit,
    canSwipe: (FromDirection) -> Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    var stagedItem by remember { mutableStateOf(item) }
    var incomingSize by remember { mutableStateOf(IntSize.Zero) }
    var swipeProgress by remember { mutableStateOf(0f) }
    val animator = remember { Animatable(0f) }
    val animation by animator.asState()
    var offsetX by remember { mutableStateOf(0f) }
    LaunchedEffect(animation) {
        offsetX = animation
    }

    LaunchedEffect(item) {
        if (item == stagedItem) return@LaunchedEffect
        animator.animateTo(1f, tween(1000))
        stagedItem = item
        onEndSwipe(item)
        animator.snapTo(0f)
        // animator.animateTo(0f, tween(1000))
    }

    Box(
        modifier = modifier.clip(shape)
            .onSizeChanged { incomingSize = it }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val isSwiping = canSwipe(if (offset.x > 0) FromDirection.Left else FromDirection.Right)
                    },
                    onDrag = { change, dragAmount ->
                        // on each drag event
                    },
                    onDragEnd = {
                        // drag finished
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = incomingSize.width * fromDirection.opposite.offset * offsetX
                    // alpha = if (animation == 0f && item != stagedItem) 0f else 1f
                }
        ) {
            content(stagedItem)
        }
        if (item == stagedItem) return@Box

        Box(
            modifier = Modifier.graphicsLayer {
                translationX = incomingSize.width * fromDirection.offset * (1 - offsetX)
            }
        ) {
            content(item)
        }
    }
}

enum class FromDirection {
    Left,
    Right;

    val offset get() = when(this) {
        Left -> -1
        Right -> 1
    }

    val opposite get() = when(this) {
        Left -> Right
        Right -> Left
    }
}