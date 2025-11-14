package pondui.ui.modifiers

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond

@Composable
fun Modifier.pad(units: Int) = this.padding(Pond.ruler.unitSpacing * units)

@Composable
fun Modifier.padVertical(units: Int) = this.padding(vertical = Pond.ruler.unitSpacing * units)

@Composable
fun Modifier.padTop(units: Int) = this.padding(top = Pond.ruler.unitSpacing * units)

@Composable
fun Modifier.padBottom(units: Int) = this.padding(bottom = Pond.ruler.unitSpacing * units)

@Composable
fun Modifier.padStart(units: Int) = this.padding(start = Pond.ruler.unitSpacing * units)

@Composable
fun Modifier.padEnd(units: Int) = this.padding(end = Pond.ruler.unitSpacing * units)