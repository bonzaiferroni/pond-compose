package pondui.ui.controls

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import pondui.utils.modifyIfTrue
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors

@Composable
fun Button(
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    ProvideSkyColors {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .modifyIfTrue(isEnabled) { clickable(onClick = onClick) }
                .graphicsLayer( alpha = if (isEnabled) 1f else .5f )
                .background(background)
                .padding(Pond.ruler.halfPadding)
        ) {
            content()
        }
    }
}
