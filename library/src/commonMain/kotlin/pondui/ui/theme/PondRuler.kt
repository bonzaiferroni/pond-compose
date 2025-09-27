package pondui.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface PondRuler {
    val spacingUnit: Float
    val shadowElevation: Dp

    val doubleSpacing: Dp get() = (spacingUnit * 2).dp
    val unitSpacing: Dp get() = spacingUnit.dp
    val doublePadding: PaddingValues get() = PaddingValues(doubleSpacing)
    val unitPadding: PaddingValues get() = PaddingValues(unitSpacing)
    val halfPadding: PaddingValues get() = PaddingValues(unitSpacing / 2)
    val buttonPadding: PaddingValues get() = PaddingValues(horizontal = doubleSpacing, vertical = unitSpacing * 1.5f)

    val rowUnit: Arrangement.Horizontal get() = Arrangement.spacedBy(unitSpacing)
    val rowSpaced: Arrangement.Horizontal get() = Arrangement.spacedBy(doubleSpacing)
    val columnUnit: Arrangement.Vertical get() = Arrangement.spacedBy(unitSpacing)
    val columnSpaced: Arrangement.Vertical get() = Arrangement.spacedBy(doubleSpacing)

    fun rowArrangement(units: Int = 1, alignment: Alignment.Horizontal = Alignment.Start) =
        Arrangement.spacedBy(unitSpacing * units, alignment)
    fun columnArrangement(units: Int = 1, alignment: Alignment.Vertical = Alignment.Top) =
        Arrangement.spacedBy(unitSpacing * units, alignment)

    val pill: Shape get() = RoundedCornerShape(percent = 100)

    val unitCorner: Dp get() = spacingUnit.dp
    val defaultCorner: Dp get() = unitCorner * 4
    val bigCorner: Dp get() = unitCorner * 4.7f
    val unitCorners: Shape get() = RoundedCornerShape(unitCorner)
    val defaultCorners: Shape get() = RoundedCornerShape(defaultCorner)
    val bigCorners: Shape get() = RoundedCornerShape(bigCorner)

    val innerCorners: Shape get() = RoundedCornerShape((unitCorner / 2))

    val roundedTop: Shape get() = RoundedCornerShape(
        topStart = unitCorner,
        topEnd = unitCorner,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    val roundedStart: Shape get() = RoundedCornerShape(
        topStart = unitCorner,
        topEnd = 0.dp,
        bottomStart = unitCorner,
        bottomEnd = 0.dp
    )

    val roundedEnd: Shape get() = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = unitCorner,
        bottomStart = 0.dp,
        bottomEnd = unitCorner
    )

    val roundedBottom: Shape get() = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = unitCorner,
        bottomEnd = unitCorner
    )

    val roundBottom: Shape get() = RoundedCornerShape(
        topStart = unitCorner,
        topEnd = unitCorner,
        bottomStart = bigCorner,
        bottomEnd = bigCorner
    )

    val roundTop: Shape get() = RoundedCornerShape(
        topStart = bigCorner,
        topEnd = bigCorner,
        bottomStart = unitCorner,
        bottomEnd = unitCorner
    )

    val roundEnd: Shape get() = RoundedCornerShape(
        topStart = unitCorner,
        topEnd = bigCorner,
        bottomStart = unitCorner,
        bottomEnd = bigCorner
    )

    val roundStart: Shape get() = RoundedCornerShape(
        topStartPercent = 50,
        topEndPercent = 0,
        bottomStartPercent = 50,
        bottomEndPercent = 0
    )

    val shroomUp: Shape get() = RoundedCornerShape(bigCorner, bigCorner, unitCorner, unitCorner)
    val shroomDown: Shape get() = RoundedCornerShape(unitCorner, unitCorner, bigCorner, bigCorner)

    val pillBottom: Shape get() = RoundedCornerShape(
        topStartPercent = 0,
        topEndPercent = 0,
        bottomStartPercent = 50,
        bottomEndPercent = 50,
    )

    val pillBottomRoundedTop: Shape get() = RoundedCornerShape(
        topStartPercent = 20,
        topEndPercent = 20,
        bottomStartPercent = 50,
        bottomEndPercent = 50,
    )
}

class DefaultRuler(density: Float): PondRuler{
    override val spacingUnit: Float = 6f * density
    override val shadowElevation = 12.dp * density
}