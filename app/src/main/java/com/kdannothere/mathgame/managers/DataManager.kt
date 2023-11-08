package com.kdannothere.mathgame.managers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kdannothere.mathgame.managers.LangManager.getLanguageCode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object DataManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

    private val musicSettingKey = booleanPreferencesKey("musicSetting")
    private val soundSettingKey = booleanPreferencesKey("soundSetting")
    private val languageSettingKey = stringPreferencesKey("languageSetting")

    suspend fun saveMusicSetting(context: Context, isMusicOn: Boolean) {
        context.dataStore.edit { data ->
            data[musicSettingKey] = isMusicOn
        }
    }

    suspend fun loadMusicSetting(context: Context): Boolean {
        return context.dataStore.data
            .map { preferences ->
                preferences[musicSettingKey] ?: true
            }
            .first()
    }

    suspend fun saveSoundSetting(context: Context, isSoundOn: Boolean) {
        context.dataStore.edit { data ->
            data[soundSettingKey] = isSoundOn
        }
    }

    suspend fun loadSoundSetting(context: Context): Boolean {
        return context.dataStore.data
            .map { preferences ->
                preferences[soundSettingKey] ?: true
            }
            .first()
    }

    suspend fun saveLanguage(context: Context, language: String) {
        context.dataStore.edit { data ->
            data[languageSettingKey] = language
        }
    }

    suspend fun loadLanguage(context: Context): String {
        return context.dataStore.data
            .map { preferences ->
                preferences[languageSettingKey] ?: getLanguageCode(context)
            }
            .first()
    }
}