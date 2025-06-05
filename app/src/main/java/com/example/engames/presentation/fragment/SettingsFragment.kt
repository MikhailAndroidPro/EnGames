package com.example.engames.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.domain.models.enums.ThemeMode
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.databinding.FragmentSettingsBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }
    override fun applyClick() {
        super.applyClick()
        with(binding) {
            deleteAccountBtn.setOnClickListener {
                deleteAccount()
            }
            logOutAccountBtn.setOnClickListener {
                logout()
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
    private fun logout(){
        App.sharedManager.logOut()
        findNavController().popBackStack(R.id.authFragment, true)
    }
    private fun setupView(){
        with(binding) {
            when (App.settingsManager.getCurrentTheme()) {
                ThemeMode.DARK -> switchTheme.isOn = true
                ThemeMode.LIGHT -> switchTheme.isOn = false
            }
            when(App.settingsManager.getCurrentLanguage()){
                "ru" -> languageSwitch.isOn = true
                "en" -> languageSwitch.isOn = false
            }
        }
    }
    private fun deleteAccount(){

    }

    private fun setRussian(){
        activity().setLanguage(resources.getString(R.string.ru).lowercase())
    }
    private fun setEnglish() {
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
    }

    private fun setLightTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                App.settingsManager.setTheme(ThemeMode.LIGHT)
            }
        }
    }
}