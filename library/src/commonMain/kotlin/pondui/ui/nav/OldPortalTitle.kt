package pondui.ui.nav

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pondui.ui.modifiers.SlideIn
import pondui.ui.controls.Text
import pondui.ui.theme.Pond


@Composable
fun OldPortalTitle(
    hoverText: String,
    title: String,
) {
    var displayedHoverText by remember { mutableStateOf(hoverText) }
    var isHoverVisible by remember { mutableStateOf(true) }
    LaunchedEffect(hoverText) {
        if (hoverText.isNotEmpty()) {
            displayedHoverText = hoverText
            isHoverVisible = true
        } else {
            isHoverVisible = false
        }
    }
    val alpha by animateFloatAsState(if (isHoverVisible) 1f else 0f)
    Text(
        text = title,
        style = Pond.typo.title.copy(textAlign = TextAlign.Center),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 25.dp, vertical = 5.dp)
            .graphicsLayer { this.alpha = 1 - alpha }
    )
    SlideIn(
        isHoverVisible,
        factor = -1f,
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        bottomStart = Pond.ruler.bigCorner,
                        bottomEnd = Pond.ruler.bigCorner
                    )
                )
                .background(Pond.colors.primary)
                .padding(horizontal = 25.dp, vertical = 5.dp)
                .graphicsLayer { this.alpha = alpha }
        ) {
            Text(
                text = displayedHoverText,
                style = Pond.typo.title.copy(textAlign = TextAlign.Center),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}