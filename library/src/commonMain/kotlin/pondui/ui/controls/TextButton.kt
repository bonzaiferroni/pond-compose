package pondui.ui.controls

import androidx.compose.runtime.Composable

@Composable
fun TextButton(
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(onClick = onClick, isEnabled = isEnabled) {
        Text(text)
    }
}