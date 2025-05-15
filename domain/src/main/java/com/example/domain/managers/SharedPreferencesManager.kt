package com.example.domain.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

const val APP_NAME = "EnGames"
const val LOGIN_CHECK = "isAuthorized"
class SharedPreferencesManager(baseContext: Context) {
    private val preferences: SharedPreferences = baseContext.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)

    fun logIn() {
        preferences.edit { putBoolean(LOGIN_CHECK, true) }
    }

    fun logOut() {
        preferences.edit { putBoolean(LOGIN_CHECK, false) }
    }

    fun checkLogIn(): Boolean {
        return preferences.getBoolean(LOGIN_CHECK, false)
    }
    fun saveThemePreference(themeMode: Int) {
        preferences.edit { putInt("isDarkTheme", themeMode) }
        preferences.edit { putBoolean("isThemeChanged", true) }
    }

    fun saveLanguagePreference(languageCode: String) {
        preferences.edit { putString("AppLanguage", languageCode) }
    }

    fun loadLanguagePreference(): String? {
        return preferences.getString("AppLanguage", "ru")
    }

    fun isThemeChanged(): Boolean {
        return preferences.getBoolean("isThemeChanged", false)
    }

    fun loadThemePreference(): Int {
        val themeMode = preferences.getInt("isDarkTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        return themeMode
    }
}