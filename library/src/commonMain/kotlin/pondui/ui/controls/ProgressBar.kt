package pondui.ui.controls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors
import pondui.ui.modifiers.ifNotNull

@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 300
    ),
    minHeight: Dp = 10.dp,
    minWidth: Dp = 100.dp,
    color: Color = Pond.colors.data,
    padding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (() -> Unit)? = null
) {
    val progress = if (progress.isNaN()) 0f else progress
    val animatedProgress by animateFloatAsState(progress, animationSpec)
    val animatedColor by animateColorAsState(color)
    val voidColor = Pond.colors.void

    Box(
        modifier = modifier.height(IntrinsicSize.Max)
            .width(IntrinsicSize.Max)
            .defaultMinSize(minHeight = minHeight, minWidth = minWidth)
            .animateContentSize()
            .drawBehind {
                val radius = size.height / 2
                val barWidth = size.width - radius
                val filledWidth = minOf(barWidth * animatedProgress, barWidth)
                drawRoundRect(color = voidColor, cornerRadius = CornerRadius(size.height))
                val barRatio = size.height / size.width
                if (animatedProgress < barRatio) {
                    drawCircle(
                        color = animatedColor,
                        radius = size.height * animatedProgress / barRatio / 2,
                        center = Offset(radius, radius)
                    )
                } else {
                    drawRoundRect(
                        color = animatedColor, // or the color of yer flag
                        size = Size(filledWidth + radius, size.height),
                        topLeft = Offset.Zero, // anchors to port side (left)
                        cornerRadius = CornerRadius(size.height)
                    )
                }
            }
    ) {
        content?.let {
            ProvideSkyColors {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                        .padding(padding)
                ) {
                    it()
                }
            }
        }
    }
}

@Composable
fun ProgressBarButton(
    progress: Float,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.unitPadding,
    minHeight: Dp = 10.dp,
    minWidth: Dp = 100.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = labelText?.let { { Label(it.uppercase(), color = Pond.localColors.content)} }
) {
    val color = when {
        isEnabled -> Pond.colors.data
        else -> Pond.colors.disabled
    }
    ProgressBar(
        progress = progress,
        color = color,
        padding = padding,
        minHeight = minHeight,
        minWidth = minWidth,
        modifier = modifier.clip(Pond.ruler.pill)
            .ifNotNull(onClick) { this.actionable(labelText, isEnabled, onClick = it) }
    ) {
        content?.invoke()
    }
}

@Composable
fun ProgressBar(
    progress: Int,
    work: Int?,
) {
    work?.takeIf { it > 0 }?.let {
        val ratio = progress / work.toFloat()
        ProgressBar(ratio) {
            Text("$progress of $work")
        }
    }
}