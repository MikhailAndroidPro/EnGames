package com.example.domain.managers

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.domain.models.enums.ThemeMode
import java.util.Locale

class SettingsManager(baseContext: Context) {
    val sharedManager = SharedPreferencesManager(baseContext)
    val locale: Locale get() = Locale(sharedManager.loadLanguagePreference() ?: "ru-RU")

    fun getCurrentTheme(): ThemeMode {
        val savedTheme = sharedManager.loadThemePreference()
        return ThemeMode.entries.firstOrNull { it.mode == savedTheme } ?: ThemeMode.LIGHT
    }

    fun setCurrentTheme(){
        AppCompatDelegate.setDefaultNightMode(sharedManager.loadThemePreference())
    }

    fun setTheme(themeMode: ThemeMode) {
        sharedManager.saveThemePreference(themeMode.mode)
        AppCompatDelegate.setDefaultNightMode(themeMode.mode)
    }

    fun getCurrentLanguage(): String {
        return sharedManager.loadLanguagePreference() ?: locale.language
    }
}