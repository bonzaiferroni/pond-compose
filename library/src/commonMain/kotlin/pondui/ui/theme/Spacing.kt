package pondui.ui.theme

import androidx.compose.runtime.Composable

enum class Spacing {
    Unit,
    Spaced,
}

@Composable
fun Spacing.toColumnArrangement() = when (this) {
    Spacing.Unit -> Pond.ruler.columnUnit
    Spacing.Spaced -> Pond.ruler.columnSpaced
}

@Composable
fun Spacing.toRowArrangement() = when (this) {
    Spacing.Unit -> Pond.ruler.rowUnit
    Spacing.Spaced -> Pond.ruler.rowSpaced
}