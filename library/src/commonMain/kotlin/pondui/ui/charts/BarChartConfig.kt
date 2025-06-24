package pondui.ui.charts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import kabinet.utils.toMetricString

@Stable
@Immutable
data class BarChartConfig<T>(
    val array: BarChartArray<T>,
    override val contentColor: Color,
    override val isAnimated: Boolean = true,
    override val glowColor: Color? = null,
    override val bottomAxis: AxisConfig.Bottom? = null,
    override val startX: Double? = null,
    override val endX: Double? = null,
    override val provideLabelX: (Double) -> String = { it.toMetricString() }
): ChartConfig