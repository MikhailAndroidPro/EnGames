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

    // Sets the login status to true.
    fun logIn() {
        preferences.edit { putBoolean(LOGIN_CHECK, true) }
    }

    // Sets the login status to false.
    fun logOut() {
        preferences.edit { putBoolean(LOGIN_CHECK, false) }
    }

    // Saves the user's unique identifier.
    fun saveUid(uuid: String) {
        preferences.edit { putString(SAVED_UUID, uuid) }
    }

    // Retrieves the saved user's unique identifier.
    fun getUid() : String? {
        return preferences.getString(SAVED_UUID, "")
    }

    // Checks if the user is logged in.
    fun checkLogIn(): Boolean {
        return preferences.getBoolean(LOGIN_CHECK, false)
    }
    // Saves the selected theme mode and marks theme as changed.
    fun saveThemePreference(themeMode: Int) {
        preferences.edit { putInt("isDarkTheme", themeMode) }
        preferences.edit { putBoolean("isThemeChanged", true) }
    }

    // Saves the selected application language.
    fun saveLanguagePreference(languageCode: String) {
        preferences.edit { putString(APP_LANGUAGE, languageCode) }
    }

    // Loads the saved application language, defaulting to "ru".
    fun loadLanguagePreference(): String? {
        return preferences.getString(APP_LANGUAGE, "ru")
    }

    // Checks if the theme has been changed by the user.
    fun isThemeChanged(): Boolean {
        return preferences.getBoolean("isThemeChanged", false)
    }
    // Loads the saved theme preference, defaulting to system theme.
    fun loadThemePreference(): Int {
        val themeMode = preferences.getInt("isDarkTheme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        return themeMode
    }
}