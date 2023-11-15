package com.kdannothere.mathgame.presentation.managers

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TimeMng {

    fun getTimeToShow(date: Date = Date()): String {
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
        return timeFormat.format(date)
    }

    fun getDateToShow(date: Date = Date()): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatDateDb(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    fun getCurrentTimeStamp(): String = Date().time.toString()
    fun getCurrentDate(): Date = Date()

    // rewrite
    fun getDateToday(): String = formatDateDb(Date())

    fun getDateYesterday(calendar: Calendar): String {
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return formatDateDb(calendar.time)
    }

    fun getStartOfToday(calendar: Calendar): String {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return formatDateDb(calendar.time)
    }
}