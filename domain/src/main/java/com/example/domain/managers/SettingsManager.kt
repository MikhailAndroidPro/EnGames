package com.example.domain.managers

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.domain.models.enums.Language
import com.example.domain.models.enums.ThemeMode
import java.util.Locale

class SettingsManager(baseContext: Context) {
    // Manages shared preferences for settings.
    val sharedManager = SharedPreferencesManager(baseContext)
    // Gets the current locale based on saved language or defaults to Russian.
    val locale: Locale get() = Locale(sharedManager.loadLanguagePreference() ?: "ru-RU")

    // Retrieves the current theme mode, defaulting to light.
    fun getCurrentTheme(): ThemeMode {
        val savedTheme = sharedManager.loadThemePreference()
        return ThemeMode.entries.firstOrNull { it.mode == savedTheme } ?: ThemeMode.LIGHT
    }

    // Applies the currently saved theme to the application.
    fun setCurrentTheme(){
        AppCompatDelegate.setDefaultNightMode(sharedManager.loadThemePreference())
    }

    // Saves and applies the specified theme mode.
    fun setTheme(themeMode: ThemeMode) {
        sharedManager.saveThemePreference(themeMode.mode)
        AppCompatDelegate.setDefaultNightMode(themeMode.mode)
    }

    // Retrieves the current language code, defaulting to the system locale's language.
    fun getCurrentLanguage(): String {
        return sharedManager.loadLanguagePreference() ?: locale.language
    }
    // Gets the ID of the currently selected language.
    fun getLanguageId(): Int {
        return Language.entries.first { sharedManager.loadLanguagePreference() == it.name }.id
    }
}