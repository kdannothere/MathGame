package com.kdannothere.mathgame.presentation.managers

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.presentation.util.englishLanguageCode
import com.kdannothere.mathgame.presentation.util.topicAddition
import com.kdannothere.mathgame.presentation.util.topicDivision
import com.kdannothere.mathgame.presentation.util.topicMultiplication
import com.kdannothere.mathgame.presentation.util.topicSubtraction
import com.kdannothere.mathgame.presentation.util.ukrainianLanguageCode
import java.util.Locale

object LangMng {

    private val appLanguages = listOf(
        englishLanguageCode,
        ukrainianLanguageCode
    )

    fun getLocalizedContext(context: Context, lang: String): Context {
        val conf = Configuration(context.resources.configuration)
        conf.setLocale(Locale(lang))
        return context.createConfigurationContext(conf)
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

    fun getTopicResId(topic: String): Int {
        return when (topic) {
            topicAddition -> R.string.addition
            topicSubtraction -> R.string.subtraction
            topicMultiplication -> R.string.multiplication
            topicDivision -> R.string.division
            else -> 0
        }
    }
}