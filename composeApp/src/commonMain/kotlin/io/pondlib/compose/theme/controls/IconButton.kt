package io.pondlib.compose.theme.controls

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import io.pondlib.compose.theme.theme.Pond
import io.pondlib.compose.utils.modifyIfTrue

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