package pondui.ui.controls

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import pondui.ui.theme.Pond

@Composable
fun TextButton(
    text: String,
    isEnabled: Boolean = true,
    background: Color = Pond.colors.primary,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(onClick = onClick, isEnabled = isEnabled, background = background, modifier = modifier) {
        Text(
            text = text.uppercase(),
            style = TextStyle(fontSize = Pond.typo.label.fontSize),
        )
    }
}