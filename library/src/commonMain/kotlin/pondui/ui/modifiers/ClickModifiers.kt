package pondui.ui.modifiers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.clickableWithoutHoverEffect(onClick: () -> Unit) =
    this.clickable(onClick = onClick, indication = null, interactionSource = remember { MutableInteractionSource() })