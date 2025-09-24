package pondui.ui.controls

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import compose.icons.tablericons.CircleX
import compose.icons.tablericons.X

@Composable
fun InProgressIndicator(
    status: UpdateStatus,
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 24.dp
) {
    when (status) {
        UpdateStatus.None -> {
            Box(modifier = Modifier.size(indicatorSize).then(modifier))
        }
        UpdateStatus.Failed -> {
            Icon(TablerIcons.X, modifier = Modifier.size(indicatorSize).then(modifier))
        }
        UpdateStatus.Done -> {
            Icon(TablerIcons.Check, modifier = Modifier.size(indicatorSize).then(modifier))
        }
        UpdateStatus.InProgress -> {
            val spin by rememberInfiniteTransition(label = "spin").animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing)),
                label = "angle"
            )

            Canvas(Modifier.size(indicatorSize).then(modifier)) {
                val stroke = Stroke(width = size.minDimension * 0.12f, cap = StrokeCap.Round)
                // faint track (optional)
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = stroke
                )
                // spinning sweep
                drawArc(
                    color = Color(0xFF1E88E5),
                    startAngle = spin,
                    sweepAngle = 90f,
                    useCenter = false,
                    style = stroke
                )
            }
        }
    }
}

enum class UpdateStatus {
    None,
    InProgress,
    Failed,
    Done,
}