package com.kdannothere.mathgame.presentation.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {

    private val dateTimeFormat: DateFormat =
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)

    fun getFormattedDateTime(): String = dateTimeFormat.format(Date().time)

    fun formatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    fun hideSoftKeyboard(view: View, fragment: Fragment) {
        val inputMethodManager =
            fragment.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    // Math: find all divisors of a number
    fun findDivisors(n: Int): List<Int> {
        val divisors = mutableListOf<Int>()
        for (i in 1..n) {
            if (n % i == 0) {
                divisors.add(i)
            }
        }
        return divisors
    }
}