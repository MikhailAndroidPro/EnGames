package com.example.engames.app

import android.app.Application
import com.example.domain.managers.SettingsManager
import com.example.domain.managers.SharedPreferencesManager
import com.example.engames.data.repository.AuthRepository
import com.example.engames.data.repository.GamesRepository
import com.example.engames.data.repository.UserRepository
import com.example.engames.providers.SupabaseProvider

/** Main application class for initializing global components. */
class App : Application() {

    /** Companion object to hold static instances accessible throughout the app. */
    companion object {
        lateinit var sharedManager: SharedPreferencesManager
        lateinit var settingsManager: SettingsManager
        private val supabaseClient get() = SupabaseProvider.client
        lateinit var authRepository: AuthRepository
        lateinit var userRepository: UserRepository
        lateinit var gamesRepository: GamesRepository
    }

    /** Called when the application is starting, before any other objects have been created. */
    override fun onCreate() {
        super.onCreate()
        sharedManager = SharedPreferencesManager(baseContext)
        settingsManager = SettingsManager(baseContext)
        authRepository = AuthRepository(supabaseClient)
        userRepository = UserRepository(supabaseClient)
        gamesRepository = GamesRepository(supabaseClient)
    }
}