package com.kdannothere.mathgame.managers

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.lifecycle.lifecycleScope
import com.kdannothere.mathgame.presentation.BaseActivity
import com.kdannothere.mathgame.presentation.MathApp
import com.kdannothere.mathgame.presentation.util.englishLanguageCode
import com.kdannothere.mathgame.presentation.util.ukrainianLanguageCode
import kotlinx.coroutines.launch
import java.util.Locale

object LangManager {

    private val appLanguages = listOf(
        englishLanguageCode,
        ukrainianLanguageCode
    )

    fun setLanguage(activity: Activity, lang: String) {
        activity.apply {
            val config = Configuration(resources.configuration)
            val locale = Locale(lang)
            config.setLocale(locale)
            resources.configuration.updateFrom(config)
        }
    }

    fun getLanguageCode(context: Context): String {
        val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
        return when (val language = locale.language) {
            in appLanguages -> language
            else -> englishLanguageCode
        }
    }
}