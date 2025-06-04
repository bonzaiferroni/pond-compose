package pondui.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface PondRuler {
    val spacingUnit: Int
    val shadowElevation: Dp

    val doubleSpacing: Dp get() = (spacingUnit * 2).dp
    val unitSpacing: Dp get() = spacingUnit.dp
    val doublePadding: PaddingValues get() = PaddingValues(doubleSpacing)
    val unitPadding: PaddingValues get() = PaddingValues(unitSpacing)

    val rowUnit: Arrangement.Horizontal get() = Arrangement.spacedBy(unitSpacing)
    val rowSpaced: Arrangement.Horizontal get() = Arrangement.spacedBy(doubleSpacing)
    val columnUnit: Arrangement.Vertical get() = Arrangement.spacedBy(unitSpacing)
    val columnSpaced: Arrangement.Vertical get() = Arrangement.spacedBy(doubleSpacing)

    fun rowArrangement(units: Int) = Arrangement.spacedBy(unitSpacing * units)
    fun columnArrangement(units: Int) = Arrangement.spacedBy(unitSpacing * units)

    val pill: Shape get() = RoundedCornerShape(percent = 100)

    val unitCorner: Dp get() = spacingUnit.dp
    val midCorner: Dp get() = unitCorner * 4
    val bigCorner: Dp get() = unitCorner * 5
    val unitCorners: Shape get() = RoundedCornerShape(unitCorner)
    val defaultCorners: Shape get() = RoundedCornerShape(midCorner)
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
        topStartPercent = 0,
        topEndPercent = 0,
        bottomStartPercent = 50,
        bottomEndPercent = 50
    )

    val roundTop: Shape get() = RoundedCornerShape(
        topStartPercent = 50,
        topEndPercent = 50,
        bottomStartPercent = 0,
        bottomEndPercent = 0
    )

    val roundEnd: Shape get() = RoundedCornerShape(
        topStartPercent = 0,
        topEndPercent = 50,
        bottomStartPercent = 0,
        bottomEndPercent = 50
    )

    val roundStart: Shape get() = RoundedCornerShape(
        topStartPercent = 50,
        topEndPercent = 0,
        bottomStartPercent = 50,
        bottomEndPercent = 0
    )

    val shroomed: Shape get() = RoundedCornerShape(bigCorner, bigCorner, unitCorner, unitCorner)
}

object DefaultRuler : PondRuler{
    override val spacingUnit: Int = 6
    override val shadowElevation = 12.dp
}