package pondui.ui.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import pondui.ui.controls.AxisTick
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Composable
fun <T> TimeChart(
    arrays: List<TimeChartArray<T>>,
    config: ChartConfig,
    modifier: Modifier = Modifier,
) {
//    val ticks = remember {
//        val earliest = arrays.minOf { array -> array.values.minOf { array.provideX(it) } }
//        generateTimeAxis(earliest)
//    }

    LineChart(
        arrays = arrays.map { array ->
            ChartArray(
                values = array.values,
                color = array.color,
                provideX = { array.provideX(it).epochSeconds.toFloat() },
                provideY = array.provideY,
                scope = array.scope,
                label = array.label,
                isBezier = array.isBezier,
                axis = array.axis,
            )
        },
        config = config,
        modifier = modifier
    )
}

@Stable
@Immutable
data class TimeChartArray<T>(
    val values: List<T>,
    val color: Color,
    val provideX: (T) -> Instant,
    val provideY: (T) -> Float,
    val scope: DataScope? = null,
    val label: String? = null,
    val isBezier: Boolean = true,
    val axis: AxisConfig.Side ? = null
)

fun generateTimeAxis(earliest: Instant, latest: Instant = Clock.System.now()): List<AxisTick> {
    val now = Clock.System.now()
    val span = latest - earliest
    val interval = when {
        span > 21.days -> 7.days
        span > 10.days -> 3.days
        span > 2.days -> 1.days
        else -> 6.hours
    }
    val tz = TimeZone.currentSystemDefault()
    val dayStart = earliest.toLocalDateTime(tz).date.atStartOfDayIn(tz)
    val timeStart = dayStart + interval * (((earliest - dayStart) / interval).toInt() + 1)
    val intervalCount = (span / interval).toInt()
    val currentYear = now.toLocalDateTime(tz).year
    return (0 until intervalCount).map { i ->
        val time = timeStart + interval * i
        val localTime = time.toLocalDateTime(tz)
        val year = localTime.year.toString()
        val date = "${localTime.monthNumber}/${localTime.dayOfMonth}"
        val day = localTime.dayOfWeek.toString().take(3)
        val label = when {
            localTime.year != currentYear -> "$day,\n$date, $year"
            time < now - 7.days -> "$day,\n$date"
            localTime.hour == 0 && localTime.minute == 0 -> day
            else -> "${localTime.hour}:${localTime.minute.toString().padStart(2, '0')}"
        }

        AxisTick(time.epochSeconds.toFloat(), label)
    }
}