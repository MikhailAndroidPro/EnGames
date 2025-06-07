package com.example.engames.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
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

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {
    override val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUserSettings()
    }

    override fun applyClick() {
        super.applyClick()
        with(binding) {
            deleteAccountBtn.setOnClickListener {
                MaterialAlertDialogBuilder(context())
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

    private fun logout() {
        App.sharedManager.logOut()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, inclusive = true)
            .build()

        findNavController().navigate(R.id.authFragment, null, navOptions)
    }

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

    private fun deleteAccount() {
        logout()
    }

    private fun setRussian() {
        val languageCode = resources.getString(R.string.ru).lowercase()
        viewModel.updateSettings(
            context(),
            App.settingsManager.getCurrentTheme().id,
            Language.entries.first { languageCode == it.name }.id
        )
        activity().setLanguage(languageCode)
    }

    private fun setEnglish() {
        val languageCode = resources.getString(R.string.en).lowercase()
        viewModel.updateSettings(
            context(),
            App.settingsManager.getCurrentTheme().id,
            Language.entries.first { languageCode == it.name }.id
        )
        activity().setLanguage(resources.getString(R.string.en).lowercase())
    }

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