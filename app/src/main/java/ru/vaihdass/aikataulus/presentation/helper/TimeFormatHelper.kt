package ru.vaihdass.aikataulus.presentation.helper

import ru.vaihdass.aikataulus.R
import ru.vaihdass.aikataulus.base.Constants.UI_DAY_PATTERN
import ru.vaihdass.aikataulus.base.Constants.UI_TIME_PATTERN
import ru.vaihdass.aikataulus.utils.ResManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class TimeFormatHelper @Inject constructor(
    private val resManager: ResManager,
) {
    private val timeFormat = SimpleDateFormat(UI_TIME_PATTERN, Locale.getDefault())
    private val dateFormat = SimpleDateFormat(UI_DAY_PATTERN, Locale.getDefault())

    fun formatTime(date: Date) = timeFormat.format(date)

    fun formatDay(date: Date): String {
        val calendar = Calendar.getInstance()
        val targetCalendar = Calendar.getInstance()
        targetCalendar.time = date

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0);
            set(Calendar.MINUTE, 0);
            set(Calendar.SECOND, 0);
            set(Calendar.MILLISECOND, 0);
        }

        targetCalendar.apply {
            set(Calendar.HOUR_OF_DAY, 0);
            set(Calendar.MINUTE, 0);
            set(Calendar.SECOND, 0);
            set(Calendar.MILLISECOND, 0)
        }

        return when {
            calendar == targetCalendar -> resManager.getString(R.string.time_today)

            calendar.apply { add(Calendar.DAY_OF_YEAR, -1) } == targetCalendar -> {
                resManager.getString(R.string.time_yesterday)
            }

            calendar.apply { add(Calendar.DAY_OF_YEAR, 2) } == targetCalendar -> {
                resManager.getString(R.string.time_tomorrow)

            }
            else -> {
                dateFormat.format(date)
            }
        }
    }

    fun formatDayTime(date: Date) = "${formatDay(date)}, ${formatTime(date)}"

    fun formatDayTimePair(startDate: Date, endDate: Date): String {
        val start = formatDayTime(startDate)
        val end = if (isSameDay(startDate, endDate)) formatTime(endDate) else formatDayTime(endDate)
        return "$start – $end"
    }

    fun isSameDay(day1: Date, day2: Date): Boolean {
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()

        calendar1.time = day1
        calendar2.time = day2

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

}