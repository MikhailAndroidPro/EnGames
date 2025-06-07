package com.example.engames.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.models.UserSettings
import com.example.domain.models.enums.Language
import com.example.domain.models.enums.ThemeMode
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentSettingsBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.SettingsViewModel
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/** Manages user settings like theme, language, and account actions. */
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {
    override val viewModel: SettingsViewModel by viewModels()

    /**
     * Fetches user settings when the view is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUserSettings()
    }
    /** Sets up click listeners for UI elements. */
    override fun applyClick() {
        super.applyClick()
        with(binding) {
            deleteAccountBtn.setOnClickListener {
                val dialog = MaterialAlertDialogBuilder(context())
                    .setTitle(resources.getString(R.string.confirmation))
                    .setMessage(resources.getString(R.string.want_delete_account))
                    .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                        viewModel.delete(context())
                        dialog.dismiss()
                    }
                    .setNegativeButton(resources.getString(R.string.cancel)){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(context().getColor(R.color.red))
            }
            logOutAccountBtn.setOnClickListener {
                viewModel.logout(context())
            }
            languageSwitch.setOnToggledListener(object : OnToggledListener {
                override fun onSwitched(toggleableView: ToggleableView?, isOn: Boolean) {
                    if (isOn) setRussian() else setEnglish()
                }
            })
            switchTheme.setOnToggledListener(object : OnToggledListener {
                override fun onSwitched(toggleableView: ToggleableView?, isOn: Boolean) {
                    if (isOn) setDarkTheme() else setLightTheme()
                }
            })
        }
    }
    /** Logs the user out and navigates to the authentication screen. */
    private fun logout() {
        App.sharedManager.logOut()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, inclusive = true)
            .build()

        findNavController().navigate(R.id.authFragment, null, navOptions)
    }
    /** Sets the UI based on locally stored settings when there's no internet. */
    private fun setSettingsNoInternet() {
        with(binding) {
            when (App.settingsManager.getCurrentTheme()) {
                ThemeMode.DARK -> switchTheme.isOn = true
                ThemeMode.LIGHT -> switchTheme.isOn = false
            }
            when (App.settingsManager.getCurrentLanguage()) {
                "ru" -> languageSwitch.isOn = true
                "en" -> languageSwitch.isOn = false
            }
        }
    }
    /** Initiates the logout process after account deletion. */
    private fun deleteAccount() {
        logout()
    }
    /** Sets the application language to Russian and updates settings. */
    private fun setRussian() {
        val languageCode = resources.getString(R.string.ru).lowercase()
        viewModel.updateSettings(
            context(),
            App.settingsManager.getCurrentTheme().id,
            Language.entries.first { languageCode == it.name }.id
        )
        activity().setLanguage(languageCode)
    }
    /** Sets the application language to English and updates settings. */
    private fun setEnglish() {
        val languageCode = resources.getString(R.string.en).lowercase()
        viewModel.updateSettings(
            context(),
            App.settingsManager.getCurrentTheme().id,
            Language.entries.first { languageCode == it.name }.id
        )
        activity().setLanguage(resources.getString(R.string.en).lowercase())
    }
    /** Sets the application theme to dark mode and updates settings. */
    private fun setDarkTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                App.settingsManager.setTheme(ThemeMode.DARK)
            }
        }
        viewModel.updateSettings(
            context(),
            ThemeMode.DARK.id,
            App.settingsManager.getLanguageId()
        )
    }
    /** Sets the application theme to light mode and updates settings. */
    private fun setLightTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                App.settingsManager.setTheme(ThemeMode.LIGHT)
            }
        }
        viewModel.updateSettings(
            context(),
            ThemeMode.LIGHT.id,
            App.settingsManager.getLanguageId()
        )
    }
    /** Observes LiveData for changes in logout, user settings, settings update, and account deletion states. */
    override fun setObservers() {
        super.setObservers()

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    logout()
                }

                is ResponseState.Error -> {
                    showToast(it.message)
                }
            }
        }
        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    if (viewModel.isFirstLaunch) setSettings(it.data) else setSettingsNoInternet()
                }

                is ResponseState.Error -> {
                    setSettingsNoInternet()
                }
            }
        }
        viewModel.stateSettings.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {}
                is ResponseState.Error -> {
                    showToast(buildString {
                        append(it.message)
                        append(getString(R.string.changes_not_save))
                    })
                }
            }
        }
        viewModel.stateDelete.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    deleteAccount()
                }

                is ResponseState.Error -> {
                    showToast(it.message)
                }
            }
        }
    }
    /** Applies user settings (theme and language) to the UI. */
    private fun setSettings(userSettings: UserSettings) {
        when (userSettings.themeId) {
            1 -> binding.switchTheme.isOn = true
            2 -> binding.switchTheme.isOn = false
        }
        when (userSettings.languageId) {
            1 -> binding.languageSwitch.isOn = false
            2 -> binding.languageSwitch.isOn = true
        }
    }
}