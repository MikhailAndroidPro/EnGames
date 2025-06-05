package com.example.engames.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.databinding.FragmentSettingsBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {
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
                    changeLanguage()
                }
            })
            switchTheme.setOnToggledListener(object : OnToggledListener {
                override fun onSwitched(toggleableView: ToggleableView?, isOn: Boolean) {
                    changeTheme()
                }
            })
        }
    }
    private fun logout(){
        App.sharedManager.logOut()
        findNavController().popBackStack(R.id.authFragment, true)
    }
    private fun deleteAccount(){

    }
    private fun changeTheme(){

    }
    private fun changeLanguage(){

    }
}