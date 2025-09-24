package pondui.ui.controls

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import pondui.ui.modifiers.ifTrue
import pondui.ui.theme.Pond

@Composable
fun IconButton(
    imageVector: ImageVector,
    tint: Color = Pond.localColors.content,
    hoverText: String? = null,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.unitPadding,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Icon(
        imageVector = imageVector,
        modifier = modifier
            .clip(Pond.ruler.defaultCorners)
            .ifTrue(isEnabled) { this.actionable(hoverText, onClick = onClick) }
            .padding(padding),
        tint = tint
    )
}