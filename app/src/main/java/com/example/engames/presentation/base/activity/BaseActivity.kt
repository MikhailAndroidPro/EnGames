package com.example.engames.presentation.base.activity

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

    private fun setSettings() {
        if (App.sharedManager.isThemeChanged()) {
            App.settingsManager.setCurrentTheme()
        }
        setLanguage(App.settingsManager.getCurrentLanguage(), true)
    }
    open fun setLanguage(languageCode: String, isLaunch: Boolean = false) {
        App.sharedManager.saveLanguagePreference(languageCode)
        updateResources(languageCode, isLaunch)
    }
    private fun updateResources(languageCode: String, isLaunch: Boolean) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
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
                R.id.splashFragment, R.id.authFragment, R.id.regFragment -> {
                    view.visibility = View.GONE
                }
                else -> {
                    view.visibility = View.VISIBLE
                }
            }
        }
    }
}