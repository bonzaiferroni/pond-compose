package pondui.ui.controls

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pondui.ui.theme.Pond

@Composable
fun RowScope.Expando(weight: Float = 1f) {
    Spacer(modifier = Modifier.weight(weight))
}

@Composable
fun RowScope.Expando(units: Int) {
    Spacer(modifier = Modifier.width(Pond.ruler.unitSpacing * units))
}

@Composable
fun ColumnScope.Expando(weight: Float = 1f) {
    Spacer(modifier = Modifier.weight(weight))
}

@Composable
fun ColumnScope.Expando(units: Int) {
    Spacer(modifier = Modifier.height(Pond.ruler.unitSpacing * units))
}