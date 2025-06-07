package com.example.domain.models.enums

import androidx.appcompat.app.AppCompatDelegate

enum class ThemeMode(val mode: Int, val id: Int) {
    DARK(AppCompatDelegate.MODE_NIGHT_YES, 1),
    LIGHT(AppCompatDelegate.MODE_NIGHT_NO, 2)
}