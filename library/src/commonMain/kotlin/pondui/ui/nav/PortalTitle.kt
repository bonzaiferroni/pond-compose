package pondui.ui.nav

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import pondui.ui.behavior.SlideIn
import pondui.ui.controls.Text
import pondui.ui.theme.Pond


@Composable
fun RowScope.PortalTitle(
    hoverText: String,
    currentRoute: NavRoute,
) {
    Box(
        modifier = Modifier.weight(1f)
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
        SlideIn(isHoverVisible, .5f) {
            Text(
                text = displayedHoverText,
                style = Pond.typo.title.copy(textAlign = TextAlign.Center),
                color = Pond.colors.shine.copy(.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
                    .graphicsLayer { this.alpha = alpha }
            )
        }
        SlideIn(!isHoverVisible, .5f) {
            Text(
                text = currentRoute.title,
                style = Pond.typo.title.copy(textAlign = TextAlign.Center),
                color = Pond.localColors.contentDim,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
                    .graphicsLayer { this.alpha = 1 - alpha }
            )
        }
    }
}