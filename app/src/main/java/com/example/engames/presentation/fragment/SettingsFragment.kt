package com.example.engames.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.domain.models.enums.ThemeMode
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentSettingsBinding
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.SettingsViewModel
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {
    override val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }
    override fun applyClick() {
        super.applyClick()
        with(binding) {
            deleteAccountBtn.setOnClickListener {
                viewModel.delete(context())
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
    private fun logout(){
        App.sharedManager.logOut()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, inclusive = true)
            .build()

        findNavController().navigate(R.id.authFragment, null, navOptions)
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
        logout()
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

    override fun setObservers() {
        super.setObservers()

        viewModel.state.observe(viewLifecycleOwner){
            when (it) {
                is ResponseState.Success -> {
                    logout()
                }

                is ResponseState.Error -> {
                    showToast(it.message)
                }
            }
        }
        viewModel.stateDelete.observe(viewLifecycleOwner){
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
}