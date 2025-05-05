package pondui.ui.controls

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors

@Composable
fun ProgressBar(
    progress: Float,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 300
    ),
    minHeight: Dp = 10.dp,
    minWidth: Dp = 80.dp,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)? = null
) {
    val animatedProgress by animateFloatAsState(progress, animationSpec)
    val color = Pond.colors.secondary
    val voidColor = Pond.colors.void

    Box(
        modifier = modifier.height(IntrinsicSize.Max)
            .width(IntrinsicSize.Max)
            .defaultMinSize(minHeight = minHeight, minWidth = minWidth)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .drawBehind {
                    val barWidth = size.width * animatedProgress
                    drawRoundRect(color = voidColor, cornerRadius = CornerRadius(size.height))
                    val barRatio = size.height / size.width
                    if (animatedProgress < barRatio) {
                        drawCircle(
                            color = color,
                            radius = size.height * animatedProgress / 2,
                            center = Offset(size.height / 2, size.height / 2)
                        )
                    } else {
                        drawRoundRect(
                            color = color, // or the color of yer flag
                            size = Size(barWidth, size.height),
                            topLeft = Offset.Zero, // anchors to port side (left)
                            cornerRadius = CornerRadius(size.height)
                        )
                    }
                }
        )
        content?.let {
            ProvideSkyColors {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                        .padding(Pond.ruler.innerPadding)
                ) {
                    it()
                }
            }
        }
    }
}