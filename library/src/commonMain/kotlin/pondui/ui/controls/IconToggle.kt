package pondui.ui.controls

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import pondui.ui.behavior.ifTrue
import pondui.ui.theme.Pond

@Composable
fun IconToggle(
    value: Boolean,
    imageVector: ImageVector,
    tint: Color = Pond.localColors.content,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onToggle: (Boolean) -> Unit,
) {
    val shadowColor = when {
        !enabled -> Color.Transparent
        value -> Pond.colors.glow
        else -> DefaultShadowColor
    }

    Icon(
        imageVector = imageVector,
        modifier = modifier
            .shadow(15.dp, shape = Pond.ruler.pill, ambientColor = shadowColor, spotColor = shadowColor)
            .background(Pond.localColors.content.copy(alpha = .2f))
            .ifTrue(enabled) { clickable { onToggle(!value) } }
            .padding(Pond.ruler.doublePadding),
        tint = when {
            !enabled -> tint.copy(.5f)
            value -> Pond.colors.glow
            else -> tint
        }
    )
}