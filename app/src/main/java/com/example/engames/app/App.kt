package com.example.engames.app

import android.app.Application
import com.example.domain.managers.SettingsManager
import com.example.domain.managers.SharedPreferencesManager

class App : Application() {

    companion object {
        lateinit var sharedManager: SharedPreferencesManager
        lateinit var settingsManager: SettingsManager
    }

    override fun onCreate() {
        super.onCreate()
        sharedManager = SharedPreferencesManager(baseContext)
        settingsManager = SettingsManager(baseContext)
    }
}