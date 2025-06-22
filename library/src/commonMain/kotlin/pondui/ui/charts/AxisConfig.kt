package pondui.ui.charts

import kabinet.utils.toMetricString
import pondui.ui.controls.AxisTick


interface AxisConfig {
    val tickCount: Int
    val toLabel: (Float) -> String

    interface Side: AxisConfig {
        val side: AxisSide
    }

    sealed interface Bottom: AxisConfig
}

data class SideAxisAutoConfig(
    override val tickCount: Int,
    override val side: AxisSide,
    override val toLabel: (Float) -> String = { it.toMetricString() }
): AxisConfig.Side

data class BottomAxisAutoConfig(
    override val tickCount: Int,
    override val toLabel: (Float) -> String = { it.toMetricString() }
): AxisConfig.Bottom

data class BottomAxisConfig(
    val ticks: List<AxisTick>
): AxisConfig.Bottom {
    override val tickCount get () = ticks.size
    override val toLabel: (Float) -> String = { value -> ticks.first { it.value == value }.label }
}

enum class AxisSide {
    Left,
    Right,
}