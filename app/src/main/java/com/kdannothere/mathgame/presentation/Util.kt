package com.kdannothere.mathgame.presentation

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

object Util {

    fun showDialog(activity: Activity, message: String) {
        val gameActivity = activity as MainActivity
        gameActivity.showDialog(message)
    }

    fun hideSoftKeyboard(view: View, fragment: Fragment) {
        val inputMethodManager =
            fragment.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}