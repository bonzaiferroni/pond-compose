package pondui.ui.theme

import androidx.compose.runtime.Composable

enum class Spacing {
    Unit,
    Grouped,
    Spaced,
}

@Composable
fun Spacing.toColumnArrangement() = when (this) {
    Spacing.Unit -> Pond.ruler.columnUnit
    Spacing.Grouped -> Pond.ruler.columnGrouped
    Spacing.Spaced -> Pond.ruler.columnSpaced
}

@Composable
fun Spacing.toRowArrangement() = when (this) {
    Spacing.Unit -> Pond.ruler.rowUnit
    Spacing.Grouped -> Pond.ruler.rowGrouped
    Spacing.Spaced -> Pond.ruler.rowSpaced
}