package pondui.ui.controls

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import pondui.utils.modifyIfTrue
import pondui.ui.theme.Pond

@Composable
fun IconButton(
    imageVector: ImageVector,
    tint: Color = Pond.localColors.content,
    hoverText: String? = null,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Icon(
        imageVector = imageVector,
        modifier = modifier
            .clip(Pond.ruler.rounded)
            .modifyIfTrue(isEnabled) { this.actionable(hoverText, onClick = onClick) }
            .padding(Pond.ruler.innerPadding),
        tint = tint
    )
}