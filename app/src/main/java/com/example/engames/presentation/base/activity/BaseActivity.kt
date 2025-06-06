package com.example.engames.presentation.base.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.engames.R
import com.example.engames.app.App
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSettings()
    }

    override fun attachBaseContext(newBase: Context) {
        fun wrapContext(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)

            return context.createConfigurationContext(config)
        }
        val lang = App.settingsManager.getCurrentLanguage()
        val context = wrapContext(newBase, lang)
        super.attachBaseContext(context)
    }
    private fun setSettings() {
        if (App.sharedManager.isThemeChanged()) {
            App.settingsManager.setCurrentTheme()
        }
        setLanguage(App.settingsManager.getCurrentLanguage(), true)
    }
    open fun setLanguage(languageCode: String, isLaunch: Boolean = false) {
        App.sharedManager.saveLanguagePreference(languageCode)
        if (!isLaunch) this.recreate()
    }
    protected fun pinNavView(idHost: Int, idNavigation: Int, view: View){
        val navHostFragment = supportFragmentManager
            .findFragmentById(idHost) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigation = findViewById<BottomNavigationView>(idNavigation)
        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.authFragment, R.id.regFragment, R.id.gameEnterFragment,
                R.id.gameConnectFragment, R.id.gameChoiceFragment , R.id.gameVictorineFragment -> {
                    view.visibility = View.GONE
                }
                else -> {
                    view.visibility = View.VISIBLE
                }
            }
        }
    }
}