package pondui.ui.charts

import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChartScope(
    val data: DataScope,
    val chartMinX: Float,
    val chartMaxX: Float,
    val chartMinY: Float,
    val chartMaxY: Float,
    val chartRangeX: Float,
    val chartRangeY: Float,
    val scaleX: Float,
    val scaleY: Float,
    val sizePx: Size,
    val density: Float,
    val axisPaddingPx: Float,
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

internal fun CacheDrawScope.gatherChartScope(
    config: ChartConfig,
    dataSpace: DataScope,
    textRuler: TextMeasurer
): ChartScope {
    val labelHeightPx = CHART_AXIS_LABEL_HEIGHT.sp.toPx()
    val axisPaddingPx = CHART_SIDE_AXIS_MARGIN.dp.toPx()
    // val bottomAxisMarginHeight = labelHeight + CHART_AXIS_PADDING.dp.toPx()
    val chartMinX = if (config.leftAxis != null) axisPaddingPx else 0f
    val chartMaxX = if (config.rightAxis != null) size.width - axisPaddingPx else size.width
    val chartMinY = labelHeightPx / 2
    val chartMaxY = if (config.bottomAxis != null) size.height - axisPaddingPx else size.height
    val chartRangeX = chartMaxX - chartMinX
    val chartRangeY = chartMaxY - chartMinY

    val dataRangeX = dataSpace.rangeX.takeIf { it != 0f } ?: 1f
    val dataRangeY = dataSpace.rangeY.takeIf { it != 0f } ?: 1f

    return ChartScope(
        data = dataSpace,
        chartMinX = chartMinX,
        chartMaxX = chartMaxX,
        chartMinY = chartMinY,
        chartMaxY = chartMaxY,
        chartRangeX = chartRangeX,
        chartRangeY = chartRangeY,
        scaleX = chartRangeX / dataRangeX,
        scaleY = chartRangeY / dataRangeY,
        sizePx = size,
        density = density,
        axisPaddingPx = axisPaddingPx,
        labelHeightPx = labelHeightPx,
        lineWidthPx = 4.dp.toPx(),
        pointRadiusPx = 8.dp.toPx(),
        labelPaddingPx = CHART_AXIS_PADDING.dp.toPx(),
        labelFontSize = CHART_AXIS_LABEL_HEIGHT.sp,
        textRuler = textRuler,
        horizontalLineWidthPx = 2.dp.toPx()
    )
}
