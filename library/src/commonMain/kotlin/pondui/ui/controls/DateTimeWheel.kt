package pondui.ui.controls

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kabinet.utils.toInstantUtc
import kabinet.utils.toLocalDateTime
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Composable
fun DateTimeWheel(
    instant: Instant,
    modifier: Modifier = Modifier,
    onChangeInstant: (Instant) -> Unit,
) {
    Row(1, modifier = modifier) {
        val time = instant.toLocalDateTime()
        TimeWheel(instant, onChangeInstant = onChangeInstant)
        Label("on")
        // month
        MenuWheel(
            selectedItem = MonthName.entries.first { it.calendarNumber == time.monthNumber },
            toLabel = { it.abbrevation },
            options = MonthName.entries.toImmutableList(),
        ) {
            onChangeInstant(LocalDateTime(time.year, it.calendarNumber, time.dayOfMonth, time.hour, time.minute).toInstantUtc())
        }
        // dayOfMonth
        MenuWheel(
            selectedItem = time.dayOfMonth,
            options = days(time.year, time.monthNumber),
            itemAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(menuPartWidth)
        ) {
            onChangeInstant(LocalDateTime(time.year, time.month, it, time.hour, time.minute).toInstantUtc())
        }
        // year
        MenuWheel(
            selectedItem = time.year,
            options = years.toImmutableList(),
        ) {
            onChangeInstant(LocalDateTime(it, time.month, time.dayOfMonth, time.hour, time.minute).toInstantUtc())
        }
    }
}

@Composable
fun TimeWheel(
    instant: Instant,
    modifier: Modifier = Modifier,
    onChangeInstant: (Instant) -> Unit,
) {
    Row(1, modifier = modifier) {
        val time = instant.toLocalDateTime()
        var isPm by remember { mutableStateOf(time.hour >= 12) }
        Row(0) {
            // hours
            MenuWheel(
                selectedItem = time.hour.to12Hour(),
                options = hours,
                modifier = Modifier.width(menuPartWidth)
            ) {
                onChangeInstant(LocalDateTime(time.year, time.month, time.dayOfMonth, it.to24Hour(isPm), time.minute).toInstantUtc())
            }
            Label(":")
            // minutes
            MenuWheel(
                selectedItem = time.minute.toString().padStart(2, '0'),
                options = minutes,
                itemAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(menuPartWidth)
            ) {
                onChangeInstant(LocalDateTime(time.year, time.month, time.dayOfMonth, time.hour, it.toInt()).toInstantUtc())
            }
        }
        // am/pm
        MenuWheel(
            selectedItem = if (isPm) "PM" else "AM",
            options = amPm,
        ) {
            isPm = it == "PM"
            onChangeInstant(LocalDateTime(time.year, time.month, time.dayOfMonth, time.hour.to24Hour(isPm), time.minute).toInstantUtc())
        }
    }
}

private val menuPartWidth = 20.dp
private val hours = (listOf(12) + (1..11)).toImmutableList()
private val minutes = (0..12).map { (it * 5).toString().padStart(2, '0') }.toImmutableList()
private fun days(year: Int, month: Int) = daysInMonth(year, month).let { (1..it).toImmutableList() }
private val years = Clock.System.now().toLocalDateTime().year.let { (it..(it + 12)) }
private val amPm = listOf("AM", "PM").toImmutableList()

private fun daysInMonth(year: Int, month: Int): Int {
    val firstOfMonth = LocalDate(year, month, 1)
    val firstOfNext  = firstOfMonth.plus(1, DateTimeUnit.MONTH)
    val lastOfMonth  = firstOfNext.minus(1, DateTimeUnit.DAY)
    return lastOfMonth.dayOfMonth
}

private fun Int.to12Hour() = when (this % 12) {
    0 -> 12
    else -> this % 12
}

private fun Int.to24Hour(isPm: Boolean): Int = (this % 12) + if (isPm) 12 else 0

private enum class MonthName(val calendarNumber: Int, val abbrevation: String) {
    January(1, "Jan"),
    February(2, "Feb"),
    March(3, "Mar"),
    April(4, "Apr"),
    May(5, "May"),
    June(6, "Jun"),
    July(7, "Jul"),
    August(8, "Aug"),
    September(9, "Sep"),
    October(10, "Oct"),
    November(11, "Nov"),
    December(12, "Dec");

    override fun toString(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}