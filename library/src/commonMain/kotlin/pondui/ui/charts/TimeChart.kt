package pondui.ui.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kabinet.utils.toDoubleMillis
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import pondui.ui.controls.BalloonAxisTick
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Composable
fun <T> TimeChart(
    arrays: List<ChartArray<T>>,
    config: ChartConfig,
    modifier: Modifier = Modifier,
    provideX: (T) -> Instant
) {
    LineChart(
        arrays = arrays,
        config = config,
        modifier = modifier,
        provideX = { provideX(it).toDoubleMillis() }
    )
}

fun generateTimeAxis(earliest: Instant, latest: Instant = Clock.System.now()): List<BalloonAxisTick> {
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

        BalloonAxisTick(time.epochSeconds.toFloat(), label)
    }
}