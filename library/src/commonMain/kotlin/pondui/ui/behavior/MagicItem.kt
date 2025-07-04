@file:Suppress("DuplicatedCode")

package pondui.ui.behavior

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun <T> MagicItem(
    item: T?,
    key: (T?) -> Any? = { item },
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    rotationY: Int = 0,
    rotationX: Int = 0,
    durationMillis: Int = 200,
    isVisibleInit: Boolean = false,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit,
    defaultContent: @Composable () -> Unit
) {
    var cachedItem by remember { mutableStateOf(item) }
    var isClearing by remember { mutableStateOf(false) }

    LaunchedEffect(key(item)) {
        if (item == cachedItem) return@LaunchedEffect
        isClearing = true
        delay((durationMillis - 10).toLong())
        cachedItem = item
        isClearing = false
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .magic(
                isVisible = !isClearing,
                offsetX = offsetX,
                offsetY = offsetY,
                rotationY = rotationY,
                rotationX = rotationX,
                durationMillis = durationMillis,
                exitOpposite = true,
                isVisibleInit = isVisibleInit,
            ),
    ) {
        val visibleItem = cachedItem
        if (visibleItem != null) {
            itemContent(visibleItem)
        } else {
            defaultContent()
        }
    }
}

@Composable
fun <T> MagicItem(
    item: T,
    key: (T) -> Any? = { item },
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    rotationY: Int = 0,
    rotationX: Int = 0,
    durationMillis: Int = 250,
    isVisibleInit: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    var cachedItem by remember { mutableStateOf(item) }
    var isClearing by remember { mutableStateOf(false) }

    LaunchedEffect(key(item)) {
        if (item == cachedItem) return@LaunchedEffect
        isClearing = true
        delay((durationMillis - 10).toLong())
        cachedItem = item
        isClearing = false
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .magic(
                isVisible = !isClearing,
                offsetX = offsetX,
                offsetY = offsetY,
                rotationY = rotationY,
                rotationX = rotationX,
                durationMillis = durationMillis,
                exitOpposite = true,
                isVisibleInit = isVisibleInit,
            )
    ) {
        content(cachedItem)
    }
}