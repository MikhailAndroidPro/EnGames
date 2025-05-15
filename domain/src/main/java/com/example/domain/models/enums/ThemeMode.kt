package com.example.domain.models.enums

import androidx.appcompat.app.AppCompatDelegate

enum class ThemeMode(val mode: Int) {
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO),
    DARK(AppCompatDelegate.MODE_NIGHT_YES)
}