package com.example.engames.presentation.fragment

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.databinding.FragmentAuthBinding
import com.example.engames.presentation.base.fragment.BaseFragment


class AuthFragment : BaseFragment<FragmentAuthBinding>(
    FragmentAuthBinding::inflate
) {
    private var isPasswordHidden = true

    override fun applyClick() {
        super.applyClick()
        with(binding) {
            eyeBtn.setOnClickListener {
                isPasswordHidden = !isPasswordHidden
                eyeBtn.setImageResource(
                    if (isPasswordHidden) R.drawable.eye_close else R.drawable.eye_open
                )
                passwordEditText.transformationMethod =
                    if (isPasswordHidden) PasswordTransformationMethod.getInstance() else null
                passwordEditText.setSelection(passwordEditText.text.length)
            }
            signUpText.setOnClickListener{
                findNavController().navigate(R.id.action_authFragment_to_regFragment)
            }
            signInButton.setOnClickListener {
                authUser()
            }
        }
    }
    private fun authUser(){
        if (binding.checkboxRememberMe.isChecked) App.sharedManager.logIn()
        findNavController().navigate(R.id.action_authFragment_to_gamesFragment)
    }
}