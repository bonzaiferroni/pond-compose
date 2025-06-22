package pondui.ui.charts

import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChartScope(
    val config: ChartConfig,
    val dimensionX: ChartDimension,
    val dimensionsY: List<ChartDimension>,
    val leftAxis: ChartAxis?,
    val rightAxis: ChartAxis?,
    val bottomAxis: ChartAxis?,
    val chartLeftMarginPx: Float,
    val chartRightMarginPx: Float,
    val chartTopMarginPx: Float,
    val chartBottomMarginPx: Float,
    val chartWidthPx: Float,
    val chartHeightPx: Float,
    val sizePx: Size,
    val density: Float,
    val axisMarginPx: Float,
    val labelHeightPx: Float,
    val labelPaddingPx: Float,
    val labelFontSize: TextUnit,
    val lineWidthPx: Float,
    val pointRadiusPx: Float,
    val textRuler: TextMeasurer,
    val horizontalLineWidthPx: Float,
) {
    fun Dp.toPx(): Float = this.value * density
}

data class ChartDimension(
    val scalePx: Float,
    val max: Double,
    val min: Double
) {
    val range get() = (max - min).toFloat()
}

internal fun <T> CacheDrawScope.gatherChartScope(
    config: ChartConfig,
    arrays: List<ChartArray<T>>,
    textRuler: TextMeasurer,
    provideX: (T) -> Double
): ChartScope {
    val labelFontSize = CHART_AXIS_LABEL_HEIGHT.sp
    val labelHeightPx = labelFontSize.toPx()
    val axisPaddingPx = CHART_SIDE_AXIS_MARGIN.dp.toPx()
    val pointRadiusPx = 8.dp.toPx()

    // vertical space
    val chartBottomMarginPx = (if (config.bottomAxis != null) axisPaddingPx else 0f) + pointRadiusPx
    val chartTopMarginPx = pointRadiusPx
    val chartHeightPx = size.height - chartTopMarginPx - chartBottomMarginPx

    val isSingularDimensionY = arrays.all { it.axis?.side != AxisSide.Left }
            || arrays.all { it.axis?.side != AxisSide.Right }
    val dataScopes = arrays.map { c -> gatherDataScope(
        array = c.values,
        floor = c.floor,
        ceiling = c.ceiling,
        provideX = provideX,
        provideY = c.provideY
    ) }
        .let { scopes ->
            val minX = scopes.minOf { it.minX }
            val maxX = scopes.maxOf { it.maxX }
            scopes.map { scope ->
                val minY = if (isSingularDimensionY) scopes.minOf { it.minY } else scope.minY
                val maxY = if (isSingularDimensionY) scopes.maxOf { it.maxY } else scope.maxY
                DataScope(maxX, minX, maxY, minY)
            }
        }

    val dimensionsY = dataScopes.map { dataScope ->
        val dataRangeY = dataScope.rangeY.takeIf { it != 0.0 } ?: 0.0
        ChartDimension(scalePx = chartHeightPx / dataRangeY.toFloat(), max = dataScope.maxY, min = dataScope.minY)
    }

    val leftAxis = gatherSideAutoAxis(AxisSide.Left, arrays, dimensionsY, textRuler, labelFontSize)
    val rightAxis = gatherSideAutoAxis(AxisSide.Right, arrays, dimensionsY, textRuler, labelFontSize)

    // horizontal space
    // val bottomAxisMarginHeight = labelHeight + CHART_AXIS_PADDING.dp.toPx()
    val chartLeftMarginPx = (leftAxis?.maxLabelWidthPx ?: 0f) + pointRadiusPx
    val chartRightMarginPx = (rightAxis?.maxLabelWidthPx ?: 0f) + pointRadiusPx
    val chartWidthPx = size.width - chartRightMarginPx - chartLeftMarginPx

    val dataMinX = dataScopes.minOf { it.minX }
    val dataMaxX = dataScopes.maxOf { it.maxX }
    val dataRangeX = dataMaxX - dataMinX
    val dimensionX = ChartDimension(scalePx = chartWidthPx / dataRangeX.toFloat(), max = dataMaxX, min = dataMinX)
    val bottomAxis = config.bottomAxis?.let {
        when (it) {
            is BottomAxisAutoConfig -> gatherAutoAxis(it, dimensionX, textRuler, config.contentColor, labelFontSize)
            is BottomAxisConfig -> gatherAxis(it.ticks, dimensionX, textRuler, config.contentColor, labelFontSize)
        }
    }

    return ChartScope(
        config = config,
        dimensionX = dimensionX,
        dimensionsY = dimensionsY,
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
        axisMarginPx = axisPaddingPx,
        labelHeightPx = labelHeightPx,
        lineWidthPx = 4.dp.toPx(),
        pointRadiusPx = pointRadiusPx,
        labelPaddingPx = CHART_AXIS_PADDING.dp.toPx(),
        labelFontSize = CHART_AXIS_LABEL_HEIGHT.sp,
        textRuler = textRuler,
        horizontalLineWidthPx = 2.dp.toPx()
    )
}
