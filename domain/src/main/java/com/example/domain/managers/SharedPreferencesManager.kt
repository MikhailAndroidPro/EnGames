package com.example.domain.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

const val APP_NAME = "EnGames"
const val APP_LANGUAGE = "AppLanguage"
const val LOGIN_CHECK = "isAuthorized"
const val SAVED_UUID = "uuid"
class SharedPreferencesManager(baseContext: Context) {
    private val preferences: SharedPreferences = baseContext.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)

    fun logIn() {
        preferences.edit { putBoolean(LOGIN_CHECK, true) }
    }

    fun logOut() {
        preferences.edit { putBoolean(LOGIN_CHECK, false) }
    }

    fun saveUid(uuid: String) {
        preferences.edit { putString(SAVED_UUID, uuid) }
    }

    fun getUid() : String? {
        return preferences.getString(SAVED_UUID, "")
    }

    fun checkLogIn(): Boolean {
        return preferences.getBoolean(LOGIN_CHECK, false)
    }
    fun saveThemePreference(themeMode: Int) {
        preferences.edit { putInt("isDarkTheme", themeMode) }
        preferences.edit { putBoolean("isThemeChanged", true) }
    }

    fun saveLanguagePreference(languageCode: String) {
        preferences.edit { putString(APP_LANGUAGE, languageCode) }
    }

    fun loadLanguagePreference(): String? {
        return preferences.getString(APP_LANGUAGE, "ru")
    }

    fun isThemeChanged(): Boolean {
        return preferences.getBoolean("isThemeChanged", false)
    }

    fun loadThemePreference(): Int {
        val themeMode = preferences.getInt("isDarkTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        return themeMode
    }
}