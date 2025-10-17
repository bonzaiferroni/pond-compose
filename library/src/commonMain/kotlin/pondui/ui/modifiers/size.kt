package pondui.ui.modifiers

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond

@Composable
fun Modifier.size(units: Int) = size(Pond.ruler.unitSpacing * units)