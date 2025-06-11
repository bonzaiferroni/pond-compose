@file:Suppress("DuplicatedCode")

package pondui.ui.behavior

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun <T> MagicItem(
    item: T?,
    durationMillis: Int = 300,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit,
    defaultContent: @Composable () -> Unit
) {
    var cachedItem by remember { mutableStateOf(item) }
    var isClearing by remember { mutableStateOf(false) }

    LaunchedEffect(item) {
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
                offsetX = 50,
                durationMillis = durationMillis,
                exitOpposite = true
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
    durationMillis: Int = 300,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    var cachedItem by remember { mutableStateOf(item) }
    var isClearing by remember { mutableStateOf(false) }

    LaunchedEffect(item) {
        if (item == cachedItem) return@LaunchedEffect
        isClearing = true
        delay((durationMillis - 10).toLong())
        cachedItem = item
        isClearing = false
    }

    Box(
        modifier = modifier
            .magic(
                isVisible = !isClearing,
                rotationX = 90,
                durationMillis = durationMillis,
                exitOpposite = true
            )
    ) {
        content(cachedItem)
    }
}