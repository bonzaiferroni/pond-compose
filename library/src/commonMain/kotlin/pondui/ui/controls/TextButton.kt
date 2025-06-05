package pondui.ui.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import pondui.ui.behavior.ifTrue
import pondui.ui.theme.Pond

@Composable
fun TextButton(
    text: String,
    style: TextStyle,
    isEnabled: Boolean = true,
    padding: PaddingValues = Pond.ruler.doublePadding,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        style = style,
        modifier = modifier.ifTrue(isEnabled) { clickable(onClick = onClick) }
            .graphicsLayer { alpha = if (isEnabled) 1f else .5f }
            .padding(padding)
    )
}