package pondui.ui.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pondui.ui.theme.Pond

@Composable
fun ProgressBar(
    progress: Float,
    height: Dp = 10.dp,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(progress)

    Box(
        modifier = modifier.height(height)
            .border(1.dp, Pond.colors.secondary)
    ) {
        Box(
            modifier = Modifier.fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .background(Pond.colors.secondary)
        )
    }
}