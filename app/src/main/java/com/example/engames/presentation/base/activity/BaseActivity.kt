package com.example.engames.presentation.base.activity

import android.content.Context
import android.content.pm.ActivityInfo
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

/**
 * Abstract base class for all activities in the application, providing common functionality.
 */
abstract class BaseActivity : AppCompatActivity() {

    /** Controller for managing app navigation within a NavHost. */
    private lateinit var navController: NavController

    /**
     * Displays a short toast message.
     * @param message The text to show.
     */
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized, this Bundle contains the most recent data.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSettings()
    }

    /**
     * Called by the system when the device configuration changes while your activity is running.
     * @param newBase The new base context for the activity.
     */
    override fun attachBaseContext(newBase: Context) {
        /** Helper function to create a new context with a specific language. */
        fun wrapContext(context: Context, language: String): Context =
            context.createConfigurationContext(Configuration(context.resources.configuration).apply { setLocale(Locale(language).also { Locale.setDefault(it) }) })
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
    /**
     * Sets the application language.
     * @param languageCode The language code (e.g., "en", "ru").
     * @param isLaunch True if called during app launch, false otherwise.
     */
    open fun setLanguage(languageCode: String, isLaunch: Boolean = false) {
        App.sharedManager.saveLanguagePreference(languageCode)
        if (!isLaunch) this.recreate()
    }
    /**
     * Sets up the bottom navigation view with a NavController and manages its visibility.
     * @param idHost The ID of the NavHostFragment.
     * @param idNavigation The ID of the BottomNavigationView.
     * @param view The BottomNavigationView instance.
     */
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
    // Lock or unlock screen orientation
    fun setOrientationUnlocked(isUnlocked: Boolean) {
        requestedOrientation = if (isUnlocked) {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}