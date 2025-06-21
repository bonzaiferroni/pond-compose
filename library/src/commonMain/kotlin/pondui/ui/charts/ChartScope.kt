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
    val dimensions: List<ChartDimension>,
    val chartMinX: Float,
    val chartMaxX: Float,
    val chartMinY: Float,
    val chartMaxY: Float,
    val chartRangeX: Float,
    val chartRangeY: Float,
    val scaleX: Float,
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
    val scaleX: Float,
    val scaleY: Float,
    val dataScope: DataScope
)

internal fun <T> CacheDrawScope.gatherChartScope(
    config: ChartConfig,
    chartArrays: List<ChartArray<T>>,
    textRuler: TextMeasurer
): ChartScope {
    val labelHeightPx = CHART_AXIS_LABEL_HEIGHT.sp.toPx()
    val axisPaddingPx = CHART_SIDE_AXIS_MARGIN.dp.toPx()
    val pointRadiusPx = 8.dp.toPx()
    val hasLeftAxis = chartArrays.any { it.axis == VerticalAxis.Left }
    val hasRightAxis = chartArrays.any { it.axis == VerticalAxis.Right }
    // val bottomAxisMarginHeight = labelHeight + CHART_AXIS_PADDING.dp.toPx()
    val chartMinX = (if (hasLeftAxis) axisPaddingPx else 0f) + pointRadiusPx
    val chartMaxX = (if (hasRightAxis) size.width - axisPaddingPx else size.width) - pointRadiusPx
    val chartMinY = (if (config.bottomAxis != null) axisPaddingPx else 0f) + pointRadiusPx
    val chartMaxY = size.height - pointRadiusPx
    val chartRangeX = chartMaxX - chartMinX
    val chartRangeY = chartMaxY - chartMinY
    val isSingularDimensionY = chartArrays.all { it.axis != VerticalAxis.Left } || chartArrays.all { it.axis != VerticalAxis.Right }

    val dataScopes = chartArrays.map { c -> c.dataScope ?: gatherDataScope(c.values, c.provideX, c.provideY) }
        .let { scopes ->
            val minX = scopes.minOf { it.minX }
            val maxX = scopes.maxOf { it.maxX }
            scopes.map { scope ->
                val minY = if (isSingularDimensionY) scopes.minOf { it.minY } else scope.minY
                val maxY = if (isSingularDimensionY) scopes.maxOf { it.maxY } else scope.maxY
                DataScope(maxX, minX, maxY, minY)
            }
        }
    val dimensions = dataScopes.map { dataScope ->
        val dataRangeX = dataScope.rangeX.takeIf { it != 0f } ?: 1f
        val dataRangeY = dataScope.rangeY.takeIf { it != 0f } ?: 1f
        ChartDimension(
            scaleX = chartRangeX / dataRangeX,
            scaleY = chartRangeY / dataRangeY,
            dataScope = dataScope
        )
    }

    val dataMinX = dimensions.minOf { it.dataScope.minX }
    val dataMaxX = dimensions.maxOf { it.dataScope.maxX }
    val dataRangeX = dataMaxX - dataMinX

    return ChartScope(
        config = config,
        dimensions = dimensions,
        chartMinX = chartMinX,
        chartMaxX = chartMaxX,
        chartMinY = chartMinY,
        chartMaxY = chartMaxY,
        chartRangeX = chartRangeX,
        chartRangeY = chartRangeY,
        scaleX = chartRangeX / dataRangeX,
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
