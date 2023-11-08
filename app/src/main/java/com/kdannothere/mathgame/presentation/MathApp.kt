package com.kdannothere.mathgame.presentation

import android.app.Application
import kotlinx.coroutines.Dispatchers

class MathApp : Application() {


    companion object {
        val dispatcherIO = Dispatchers.IO
        val dispatcherMain = Dispatchers.Main
    }
}