package pondui.ui.charts

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kabinet.utils.toMetricString
import kotlin.math.roundToInt

data class BarChartScope(
    override val config: ChartConfig,
    override val dimensionX: ChartDimension,
    val dimensionY: ChartDimension,
    override val leftAxis: ChartAxis?,
    override val rightAxis: ChartAxis?,
    override val bottomAxis: ChartAxis?,
    override val chartLeftMarginPx: Float,
    override val chartRightMarginPx: Float,
    override val chartTopMarginPx: Float,
    override val chartBottomMarginPx: Float,
    override val chartWidthPx: Float,
    override val chartHeightPx: Float,
    override val sizePx: Size,
    override val density: Float,
    override val axisMarginPx: Float,
    override val labelHeightPx: Float,
    override val labelPaddingPx: Float,
    override val labelFontSize: TextUnit,
    override val textRuler: TextMeasurer,
    override val horizontalLineWidthPx: Float,
    val barWidthPx: Float,
) : ChartScope

@Stable
@Immutable
data class BarChartArray<T>(
    val values: List<T>,
    val interval: Double,
    val label: String? = null,
    val axis: AxisConfig.Side ? = null,
    val floor: Double? = null,
    val ceiling: Double? = null,
    val provideLabelY: (Double) -> String = { it.toMetricString() },
    val provideX: (T) -> Double,
    val provideY: (T) -> Double,
    val provideColor: (T) -> Color,
)

internal fun <T> CacheDrawScope.gatherBarChartScope(
    config: ChartConfig,
    array: BarChartArray<T>,
    textRuler: TextMeasurer,
): BarChartScope {
    val labelFontSize = CHART_AXIS_LABEL_HEIGHT.sp
    val labelHeightPx = labelFontSize.toPx()
    val bottomMarginPx = CHART_BOTTOM_MARGIN.dp.toPx()
    val pointRadiusPx = 8.dp.toPx()

    // vertical space
    val chartBottomMarginPx = (if (config.bottomAxis != null) labelHeightPx * 1.5f else 0f) + pointRadiusPx
    val chartTopMarginPx = pointRadiusPx
    val chartHeightPx = size.height - chartTopMarginPx - chartBottomMarginPx

    val dataScope = gatherDataScope(
        array = array.values,
        floor = array.floor,
        ceiling = array.ceiling,
        startX = config.startX,
        endX = config.endX,
        provideX = array.provideX,
        provideY = array.provideY
    )

    val dataRangeY = dataScope.rangeY.takeIf { it != 0.0 } ?: 0.0
    val dimensionY = ChartDimension(
        scalePx = chartHeightPx / dataRangeY.toFloat(),
        max = dataScope.maxY,
        min = dataScope.minY
    )

    val axis = array.axis?.let { gatherAutoAxis(
        axisConfig = it,
        dimension = dimensionY,
        textRuler = textRuler,
        color = array.provideColor(array.values.first()),
        fontSize = labelFontSize,
        toLabel = array.provideLabelY
    ) }
    val leftAxis = if (array.axis?.side == AxisSide.Left) axis else null
    val rightAxis = if (array.axis?.side == AxisSide.Right) axis else null

    val chartLeftMarginPx = (axis?.takeIf { array.axis.side == AxisSide.Left }
        ?.maxLabelWidthPx?.let { it + labelHeightPx / 2 } ?: 0f) + pointRadiusPx
    val chartRightMarginPx = (axis?.takeIf { array.axis.side == AxisSide.Right }
        ?.maxLabelWidthPx?.let { it + labelHeightPx / 2 } ?: 0f) + pointRadiusPx
    val chartWidthPx = size.width - chartRightMarginPx - chartLeftMarginPx

    val dataRangeX = (dataScope.maxX - dataScope.minX).takeIf { it > 0 } ?: 1.0
    val barCount = (dataRangeX / array.interval).roundToInt() + 1
    val barWidthPx = chartWidthPx / barCount

    val dimensionX = ChartDimension(scalePx = (chartWidthPx - barWidthPx) / dataRangeX.toFloat(), max = dataScope.maxX, min = dataScope.minX)
    val bottomAxis = config.bottomAxis?.let {
        when (it) {
            is BottomAxisAutoConfig -> gatherAutoAxis(it, dimensionX, textRuler, config.contentColor, labelFontSize, config.provideLabelX)
            is BottomAxisConfig -> gatherAxis(it.ticks, dimensionX, textRuler, config.contentColor, labelFontSize)
        }
    }

    return BarChartScope(
        config = config,
        dimensionX = dimensionX,
        leftAxis = leftAxis,
        rightAxis = rightAxis,
        bottomAxis = bottomAxis,
        chartLeftMarginPx = chartLeftMarginPx,
        chartRightMarginPx = chartRightMarginPx,
        chartTopMarginPx = chartTopMarginPx,
        chartBottomMarginPx = chartBottomMarginPx,
        chartWidthPx = chartWidthPx,
        chartHeightPx = chartHeightPx,
        sizePx = size,
        density = density,
        axisMarginPx = bottomMarginPx,
        labelHeightPx = labelHeightPx,
        labelPaddingPx = CHART_AXIS_PADDING.dp.toPx(),
        labelFontSize = labelFontSize,
        textRuler = textRuler,
        horizontalLineWidthPx = 3.dp.toPx(),
        dimensionY = dimensionY,
        barWidthPx = barWidthPx,
    )
}

internal const val CHART_BAR_GAP_WIDTH = 1