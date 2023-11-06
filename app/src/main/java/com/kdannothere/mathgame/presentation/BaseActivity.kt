package com.kdannothere.mathgame.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kdannothere.mathgame.managers.DataManager
import com.kdannothere.mathgame.managers.LangManager
import kotlinx.coroutines.launch

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("MyLog - Base - onCreate")
        lifecycleScope.launch {
            loadLocale()
        }
    }

    private suspend fun loadLocale() {
        LangManager.setLanguage(
            activity = this@BaseActivity,
            lang = DataManager.loadLanguage(this@BaseActivity)
        )
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        println("MyLog - Base - finish")
    }
}
