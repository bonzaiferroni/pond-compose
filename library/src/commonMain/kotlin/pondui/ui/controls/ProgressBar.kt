package pondui.ui.controls

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.theme.Pond

@Composable
fun ProgressBar(
    progress: Float,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 300
    ),
    minHeight: Dp = 10.dp,
    modifier: Modifier = Modifier.fillMaxWidth(),
    content: @Composable (() -> Unit)? = null
) {
    val animatedProgress by animateFloatAsState(progress, animationSpec)
    val color = Pond.colors.secondary

    Box(
        modifier = modifier.height(minHeight)
            .border(1.dp, color)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .drawBehind {
                    val barWidth = size.width * animatedProgress
                    drawRect(
                        color = color, // or the color of yer flag
                        size = Size(barWidth, size.height),
                        topLeft = Offset.Zero // anchors to port side (left)
                    )
                }
        )
        content?.invoke()
    }
}