package pondui.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScrollHeader(
    maxHeight: Dp = 200.dp,
    minHeight: Dp = 56.dp,
    headerContent: @Composable BoxScope.(Dp) -> Unit,
    content: @Composable (LazyListState) -> Unit
) {
    val density = LocalDensity.current
    val maxHeightPx = with(density) { maxHeight.toPx() }
    val minHeightPx = with(density) { minHeight.toPx() }
    var headerHeightPx by remember { mutableStateOf(maxHeightPx) }
    val headerHeight = with (density) { headerHeightPx.dp }

    val nested = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < 0f && headerHeightPx > minHeightPx) {
                    val shrinkPx = (-available.y).coerceAtMost(headerHeightPx - minHeightPx)
                    headerHeightPx -= shrinkPx
                    return Offset(0f, -shrinkPx)
                }
                return Offset.Zero
            }
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                if (available.y > 0f && headerHeightPx < maxHeightPx) {
                    val growPx = available.y.coerceAtMost(maxHeightPx - headerHeightPx)
                    headerHeightPx += growPx
                    return Offset(0f, growPx)
                }
                return Offset.Zero
            }
        }
    }

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize()
            .nestedScroll(nested)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val ev = awaitPointerEvent(PointerEventPass.Initial)
                        if (lazyListState.canScrollBackward) continue
                        ev.changes.forEach { ch ->
                            // 1) Mouse‐wheel delta
                            val wheelY = ch.scrollDelta.y
                            if (wheelY != 0f) {
                                if (wheelY < 0f && headerHeightPx < maxHeightPx) {
                                    val grow = (-wheelY * 100).coerceAtMost(maxHeightPx - headerHeightPx)
                                    headerHeightPx += grow
                                    ch.consume()
                                }
                            }
                        }
                    }
                }
            },
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(headerHeight)
        ) {
            headerContent(headerHeight)
        }
        content(lazyListState)
    }
}

@Composable
fun ExampleScrollHeader() {

    ScrollHeader(
        headerContent = { dp ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text("Yo Ho Ho! $dp", modifier = Modifier.align(Alignment.Center), color = Color.White)
            }
        },

    ) { lazyListState ->
        LazyColumn(
            Modifier.fillMaxSize(),
             state = lazyListState
        ) {
            items((0..100).toList()) { idx ->
                Text("Item #$idx", modifier = Modifier.padding(16.dp))
            }
        }
    }
}