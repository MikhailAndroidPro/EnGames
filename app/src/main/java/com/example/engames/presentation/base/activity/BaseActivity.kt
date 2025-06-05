package com.example.engames.presentation.base.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.engames.app.App
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var navView: View

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSettings()
    }
    fun showNavigationView(){
        navView.visibility = View.VISIBLE
    }
    fun hideNavigationView(){
        navView.visibility = View.GONE
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
    protected fun pinNavView(view: View){
        navView = view
    }
}