package pondui.ui.modifiers

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond

@Composable
fun Modifier.padTop(units: Int) = this.padding(top = Pond.ruler.unitSpacing * units)

@Composable
fun Modifier.padBottom(units: Int) = this.padding(bottom = Pond.ruler.unitSpacing * units)