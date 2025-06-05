package com.example.engames.app

import android.app.Application
import com.example.domain.managers.SettingsManager
import com.example.domain.managers.SharedPreferencesManager
import com.example.engames.providers.SupabaseProvider

class App : Application() {

    companion object {
        lateinit var sharedManager: SharedPreferencesManager
        lateinit var settingsManager: SettingsManager
        val supabaseClient get() = SupabaseProvider.client
    }

    override fun onCreate() {
        super.onCreate()
        sharedManager = SharedPreferencesManager(baseContext)
        settingsManager = SettingsManager(baseContext)
    }
}