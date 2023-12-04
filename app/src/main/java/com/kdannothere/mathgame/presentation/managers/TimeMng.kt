package com.kdannothere.mathgame.presentation.managers

import android.app.Activity
import android.app.DatePickerDialog
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

    fun getDateToday(): String {
        val newCalendar = Calendar.getInstance()
        return formatDateDb(newCalendar.time)
    }

    fun getDateYesterday(): String {
        val newCalendar = Calendar.getInstance()
        newCalendar.add(Calendar.DAY_OF_YEAR, -1)
        return formatDateDb(newCalendar.time)
    }

    fun getDateOneDayBefore(calendar: Calendar): String {
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return formatDateDb(calendar.time)
    }

    fun getDateOneDayAfter(calendar: Calendar): String {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return formatDateDb(calendar.time)
    }

    fun showDialog(
        calendar: Calendar,
        activity: Activity,
        changeDate: (newDate: String) -> Unit
    ) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            activity,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                changeDate(calendar.time.time.toString())
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}