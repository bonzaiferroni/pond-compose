package pondui.ui.charts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pondui.ui.behavior.ifTrue
import pondui.ui.controls.Column
import pondui.ui.controls.FlowRow
import pondui.ui.controls.Row
import pondui.ui.controls.Section
import pondui.ui.controls.Text
import pondui.ui.theme.Pond

@Composable
fun <T> LineChartWithLegend(
    config: LineChartConfig<T>,
    modifier: Modifier = Modifier,
    onHoverPoint: (@Composable (HoverInfo<T>?) -> Unit)? = null
) {
    var hoveredItem by remember { mutableStateOf<HoverInfo<T>?>(null)}
    Column(1, modifier = modifier) {
        Section {
            LineChart(
                config = config.copy(onHoverPoint = { hoveredItem = it }),
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
        }
        val legendValues = remember { config.arrays.mapNotNull {
            val label = it.label ?: return@mapNotNull null
            Pair(label, it.color)
        } }
        legendValues.takeIf { it.isNotEmpty() }?.let {
            FlowRow(1, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                it.forEachIndexed { index, (label, color) ->
                    Text(
                        text = label,
                        color = color,
                        modifier = Modifier.ifTrue(index + 1 < it.size) { padding(end = Pond.ruler.unitSpacing) })
                }
            }
        }
        onHoverPoint?.let {
            it(hoveredItem)
        }
    }

}