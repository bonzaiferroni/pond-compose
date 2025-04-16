package pondui.ui.controls

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import pondui.utils.modifyIfTrue
import pondui.ui.theme.Pond
import pondui.ui.theme.ProvideSkyColors

@Composable
fun Button(
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val bg = when(isEnabled) {
        true -> background
        false -> background.copy(.5f)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .modifyIfTrue(isEnabled) { clickable(onClick = onClick) }
            .background(bg)
            .padding(Pond.ruler.halfPadding)
    ) {
        ProvideSkyColors {
            content()
        }
    }
}
